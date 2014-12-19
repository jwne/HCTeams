package com.mongodb;

import org.bson.util.*;
import org.bson.*;
import java.util.logging.*;
import javax.security.auth.callback.*;
import javax.security.sasl.*;
import java.net.*;
import org.ietf.jgss.*;
import com.mongodb.util.*;
import java.io.*;
import java.util.*;
import java.security.spec.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.*;

@Deprecated
public class DBPort implements Connection
{
    public static final int PORT = 27017;
    static final boolean USE_NAGLE = false;
    static final long CONN_RETRY_TIME_MS = 15000L;
    private static Logger _rootLogger;
    private volatile boolean closed;
    private final long openedAt;
    private volatile long lastUsedAt;
    private final int generation;
    private final PooledConnectionProvider provider;
    private final ServerAddress _sa;
    private final ServerAddress _addr;
    private final MongoOptions _options;
    private final Logger _logger;
    private final DBDecoder _decoder;
    private volatile Socket _socket;
    private volatile InputStream _in;
    private volatile OutputStream _out;
    private final Set<String> authenticatedDatabases;
    private volatile long usageCount;
    private volatile ActiveState _activeState;
    
    public DBPort(final ServerAddress addr) {
        this(addr, null, new MongoOptions(), 0);
    }
    
    DBPort(final ServerAddress addr, final PooledConnectionProvider pool, final MongoOptions options, final int generation) {
        super();
        this.authenticatedDatabases = Collections.synchronizedSet(new HashSet<String>());
        this._options = options;
        this._sa = addr;
        this._addr = addr;
        this.provider = pool;
        this.generation = generation;
        this._logger = Logger.getLogger(DBPort._rootLogger.getName() + "." + addr.toString());
        try {
            this.ensureOpen();
            this._decoder = this._options.dbDecoderFactory.create();
            this.openedAt = System.currentTimeMillis();
            this.lastUsedAt = this.openedAt;
        }
        catch (IOException e) {
            throw new MongoException.Network("Exception opening the socket", e);
        }
    }
    
    public int getGeneration() {
        return this.generation;
    }
    
    public long getOpenedAt() {
        return this.openedAt;
    }
    
    public long getLastUsedAt() {
        return this.lastUsedAt;
    }
    
    Response call(final OutMessage msg, final DBCollection coll) throws IOException {
        Assertions.isTrue("open", !this.closed);
        return this.call(msg, coll, null);
    }
    
    Response call(final OutMessage msg, final DBCollection coll, final DBDecoder decoder) throws IOException {
        Assertions.isTrue("open", !this.closed);
        return this.doOperation((Operation<Response>)new Operation<Response>() {
            public Response execute() throws IOException {
                DBPort.this.setActiveState(new ActiveState(msg));
                msg.prepare();
                msg.pipe(DBPort.this._out);
                return new Response(DBPort.this._sa, coll, DBPort.this._in, (decoder == null) ? DBPort.this._decoder : decoder);
            }
        });
    }
    
    void say(final OutMessage msg) throws IOException {
        Assertions.isTrue("open", !this.closed);
        this.doOperation((Operation<Object>)new Operation<Void>() {
            public Void execute() throws IOException {
                DBPort.this.setActiveState(new ActiveState(msg));
                msg.prepare();
                msg.pipe(DBPort.this._out);
                return null;
            }
        });
    }
    
    synchronized <T> T doOperation(final Operation<T> operation) throws IOException {
        Assertions.isTrue("open", !this.closed);
        ++this.usageCount;
        try {
            return operation.execute();
        }
        catch (IOException ioe) {
            this.close();
            throw ioe;
        }
        finally {
            this.lastUsedAt = System.currentTimeMillis();
            this._activeState = null;
        }
    }
    
    void setActiveState(final ActiveState activeState) {
        Assertions.isTrue("open", !this.closed);
        this._activeState = activeState;
    }
    
    synchronized CommandResult getLastError(final DB db, final WriteConcern concern) throws IOException {
        Assertions.isTrue("open", !this.closed);
        return this.runCommand(db, concern.getCommand());
    }
    
    private synchronized Response findOne(final DB db, final String coll, final DBObject q) throws IOException {
        final OutMessage msg = OutMessage.query(db.getCollection(coll), 0, 0, -1, q, null, 4194304);
        try {
            return this.call(msg, db.getCollection(coll), null);
        }
        finally {
            msg.doneWithMessage();
        }
    }
    
    synchronized CommandResult runCommand(final DB db, final DBObject cmd) throws IOException {
        Assertions.isTrue("open", !this.closed);
        final Response res = this.findOne(db, "$cmd", cmd);
        return this.convertToCommandResult(cmd, res);
    }
    
    private CommandResult convertToCommandResult(final DBObject cmd, final Response res) {
        if (res.size() == 0) {
            return null;
        }
        if (res.size() > 1) {
            throw new MongoInternalException("something is wrong.  size:" + res.size());
        }
        final DBObject data = res.get(0);
        if (data == null) {
            throw new MongoInternalException("something is wrong, no command result");
        }
        final CommandResult cr = new CommandResult(res.serverUsed());
        cr.putAll(data);
        return cr;
    }
    
    synchronized CommandResult tryGetLastError(final DB db, final long last, final WriteConcern concern) throws IOException {
        Assertions.isTrue("open", !this.closed);
        if (last != this.usageCount) {
            return null;
        }
        return this.getLastError(db, concern);
    }
    
    OutputStream getOutputStream() throws IOException {
        Assertions.isTrue("open", !this.closed);
        return this._out;
    }
    
    InputStream getInputStream() throws IOException {
        Assertions.isTrue("open", !this.closed);
        return this._in;
    }
    
    public synchronized void ensureOpen() throws IOException {
        if (this._socket != null) {
            return;
        }
        long sleepTime = 100L;
        long maxAutoConnectRetryTime = 15000L;
        if (this._options.maxAutoConnectRetryTime > 0L) {
            maxAutoConnectRetryTime = this._options.maxAutoConnectRetryTime;
        }
        boolean successfullyConnected = false;
        final long start = System.currentTimeMillis();
        do {
            try {
                (this._socket = this._options.socketFactory.createSocket()).connect(this._addr.getSocketAddress(), this._options.connectTimeout);
                this._socket.setTcpNoDelay(true);
                this._socket.setKeepAlive(this._options.socketKeepAlive);
                this._socket.setSoTimeout(this._options.socketTimeout);
                this._in = new BufferedInputStream(this._socket.getInputStream());
                this._out = this._socket.getOutputStream();
                successfullyConnected = true;
            }
            catch (IOException e) {
                this.close();
                if (!this._options.autoConnectRetry || (this.provider != null && !this.provider.hasWorked())) {
                    throw e;
                }
                final long waitSoFar = System.currentTimeMillis() - start;
                if (waitSoFar >= maxAutoConnectRetryTime) {
                    throw e;
                }
                if (sleepTime + waitSoFar > maxAutoConnectRetryTime) {
                    sleepTime = maxAutoConnectRetryTime - waitSoFar;
                }
                this._logger.log(Level.WARNING, "Exception connecting to " + this.serverAddress().getHost() + ": " + e + ".  Total wait time so far is " + waitSoFar + " ms.  Will retry after sleeping for " + sleepTime + " ms.");
                ThreadUtil.sleep(sleepTime);
                sleepTime *= 2L;
            }
        } while (!successfullyConnected);
    }
    
    public int hashCode() {
        return this._addr.hashCode();
    }
    
    public String host() {
        return this._addr.toString();
    }
    
    public ServerAddress serverAddress() {
        return this._sa;
    }
    
    public String toString() {
        return "{DBPort  " + this.host() + "}";
    }
    
    ActiveState getActiveState() {
        Assertions.isTrue("open", !this.closed);
        return this._activeState;
    }
    
    int getLocalPort() {
        Assertions.isTrue("open", !this.closed);
        return (this._socket != null) ? this._socket.getLocalPort() : -1;
    }
    
    ServerAddress getAddress() {
        return this._addr;
    }
    
    public boolean isClosed() {
        return this.closed;
    }
    
    public void close() {
        this.closed = true;
        this.authenticatedDatabases.clear();
        if (this._socket != null) {
            try {
                this._socket.close();
            }
            catch (Exception ex) {}
        }
        this._in = null;
        this._out = null;
        this._socket = null;
    }
    
    CommandResult authenticate(final Mongo mongo, final MongoCredential credentials) {
        MongoCredential actualCredentials;
        if (credentials.getMechanism() == null) {
            if (mongo.getConnector().getServerDescription(this.getAddress()).getVersion().compareTo(new ServerVersion(2, 7)) >= 0) {
                actualCredentials = MongoCredential.createScramSha1Credential(credentials.getUserName(), credentials.getSource(), credentials.getPassword());
            }
            else {
                actualCredentials = MongoCredential.createMongoCRCredential(credentials.getUserName(), credentials.getSource(), credentials.getPassword());
            }
        }
        else {
            actualCredentials = credentials;
        }
        Authenticator authenticator;
        if (actualCredentials.getMechanism().equals("MONGODB-CR")) {
            authenticator = new NativeAuthenticator(mongo, actualCredentials);
        }
        else if (actualCredentials.getMechanism().equals("GSSAPI")) {
            authenticator = new GSSAPIAuthenticator(mongo, actualCredentials);
        }
        else if (actualCredentials.getMechanism().equals("PLAIN")) {
            authenticator = new PlainAuthenticator(mongo, actualCredentials);
        }
        else if (actualCredentials.getMechanism().equals("MONGODB-X509")) {
            authenticator = new X509Authenticator(mongo, actualCredentials);
        }
        else {
            if (!actualCredentials.getMechanism().equals("SCRAM-SHA-1")) {
                throw new IllegalArgumentException("Unsupported authentication protocol: " + actualCredentials.getMechanism());
            }
            authenticator = new ScramSha1Authenticator(mongo, actualCredentials);
        }
        final CommandResult res = authenticator.authenticate();
        this.authenticatedDatabases.add(actualCredentials.getSource());
        return res;
    }
    
    void checkAuth(final Mongo mongo) throws IOException {
        final Set<String> unauthenticatedDatabases = new HashSet<String>(mongo.getAuthority().getCredentialsStore().getDatabases());
        unauthenticatedDatabases.removeAll(this.authenticatedDatabases);
        for (final String databaseName : unauthenticatedDatabases) {
            this.authenticate(mongo, mongo.getAuthority().getCredentialsStore().get(databaseName));
        }
    }
    
    public DBPortPool getPool() {
        return null;
    }
    
    public long getUsageCount() {
        return this.usageCount;
    }
    
    PooledConnectionProvider getProvider() {
        return this.provider;
    }
    
    Set<String> getAuthenticatedDatabases() {
        return Collections.unmodifiableSet((Set<? extends String>)this.authenticatedDatabases);
    }
    
    static {
        DBPort._rootLogger = Logger.getLogger("com.mongodb.port");
    }
    
    class ActiveState
    {
        private final String namespace;
        private final OutMessage.OpCode opCode;
        private final DBObject query;
        private int numDocuments;
        private final long startTime;
        private final String threadName;
        
        ActiveState(final OutMessage outMessage) {
            super();
            this.namespace = outMessage.getNamespace();
            this.opCode = outMessage.getOpCode();
            this.query = outMessage.getQuery();
            this.numDocuments = outMessage.getNumDocuments();
            this.startTime = System.nanoTime();
            this.threadName = Thread.currentThread().getName();
        }
        
        String getNamespace() {
            return this.namespace;
        }
        
        OutMessage.OpCode getOpCode() {
            return this.opCode;
        }
        
        DBObject getQuery() {
            return this.query;
        }
        
        int getNumDocuments() {
            return this.numDocuments;
        }
        
        long getStartTime() {
            return this.startTime;
        }
        
        String getThreadName() {
            return this.threadName;
        }
    }
    
    class PlainAuthenticator extends SaslAuthenticator
    {
        private static final String MECHANISM = "PLAIN";
        private static final String DEFAULT_PROTOCOL = "mongodb";
        
        PlainAuthenticator(final Mongo mongo, final MongoCredential credentials) {
            super(mongo, credentials);
        }
        
        protected SaslClient createSaslClient() {
            try {
                return Sasl.createSaslClient(new String[] { "PLAIN" }, this.credential.getUserName(), "mongodb", DBPort.this.serverAddress().getHost(), null, new CallbackHandler() {
                    public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                        for (final Callback callback : callbacks) {
                            if (callback instanceof PasswordCallback) {
                                ((PasswordCallback)callback).setPassword(PlainAuthenticator.this.credential.getPassword());
                            }
                            else if (callback instanceof NameCallback) {
                                ((NameCallback)callback).setName(PlainAuthenticator.this.credential.getUserName());
                            }
                        }
                    }
                });
            }
            catch (SaslException e) {
                throw new MongoException("Exception initializing SASL client", e);
            }
        }
        
        public String getMechanismName() {
            return "PLAIN";
        }
    }
    
    class GSSAPIAuthenticator extends SaslAuthenticator
    {
        public static final String GSSAPI_OID = "1.2.840.113554.1.2.2";
        public static final String GSSAPI_MECHANISM = "GSSAPI";
        public static final String SERVICE_NAME_KEY = "SERVICE_NAME";
        public static final String SERVICE_NAME_DEFAULT_VALUE = "mongodb";
        public static final String CANONICALIZE_HOST_NAME_KEY = "CANONICALIZE_HOST_NAME";
        
        GSSAPIAuthenticator(final Mongo mongo, final MongoCredential credentials) {
            super(mongo, credentials);
            if (!this.credential.getMechanism().equals("GSSAPI")) {
                throw new MongoException("Incorrect mechanism: " + this.credential.getMechanism());
            }
        }
        
        protected SaslClient createSaslClient() {
            try {
                final Map<String, Object> props = new HashMap<String, Object>();
                props.put("javax.security.sasl.credentials", this.getGSSCredential(this.credential.getUserName()));
                return Sasl.createSaslClient(new String[] { "GSSAPI" }, this.credential.getUserName(), this.credential.getMechanismProperty("SERVICE_NAME", "mongodb"), this.getHostName(), props, null);
            }
            catch (SaslException e) {
                throw new MongoException("Exception initializing SASL client", e);
            }
            catch (GSSException e2) {
                throw new MongoException("Exception initializing GSSAPI credentials", e2);
            }
            catch (UnknownHostException e3) {
                throw new MongoException("Unknown host " + DBPort.this.serverAddress().getHost(), e3);
            }
        }
        
        public String getMechanismName() {
            return "GSSAPI";
        }
        
        private String getHostName() throws UnknownHostException {
            return this.credential.getMechanismProperty("CANONICALIZE_HOST_NAME", false) ? InetAddress.getByName(DBPort.this.serverAddress().getHost()).getCanonicalHostName() : DBPort.this.serverAddress().getHost();
        }
        
        private GSSCredential getGSSCredential(final String userName) throws GSSException {
            final Oid krb5Mechanism = new Oid("1.2.840.113554.1.2.2");
            final GSSManager manager = GSSManager.getInstance();
            final GSSName name = manager.createName(userName, GSSName.NT_USER_NAME);
            return manager.createCredential(name, Integer.MAX_VALUE, krb5Mechanism, 1);
        }
    }
    
    class ScramSha1Authenticator extends SaslAuthenticator
    {
        ScramSha1Authenticator(final Mongo mongo, final MongoCredential credential) {
            super(mongo, credential);
            if (!this.credential.getMechanism().equals("SCRAM-SHA-1")) {
                throw new MongoException("Incorrect mechanism: " + this.credential.getMechanism());
            }
        }
        
        protected SaslClient createSaslClient() {
            return new ScramSha1SaslClient(this.credential);
        }
        
        public String getMechanismName() {
            return "SCRAM-SHA-1";
        }
        
        class ScramSha1SaslClient implements SaslClient
        {
            private static final String gs2Header = "n,,";
            private static final int randomLength = 24;
            private final Base64Codec base64Codec;
            private final MongoCredential credential;
            private String clientFirstMessageBare;
            private String rPrefix;
            private byte[] serverSignature;
            private int step;
            
            ScramSha1SaslClient(final MongoCredential credential) {
                super();
                this.credential = credential;
                this.base64Codec = new Base64Codec();
            }
            
            public String getMechanismName() {
                return "SCRAM-SHA-1";
            }
            
            public boolean hasInitialResponse() {
                return true;
            }
            
            public byte[] evaluateChallenge(final byte[] challenge) throws SaslException {
                if (this.step == 0) {
                    ++this.step;
                    return this.computeClientFirstMessage();
                }
                if (this.step == 1) {
                    ++this.step;
                    return this.computeClientFinalMessage(challenge);
                }
                if (this.step != 2) {
                    throw new SaslException("Too many steps involved in the SCRAM-SHA-1 negotiation.");
                }
                ++this.step;
                final String serverResponse = this.encodeUTF8(challenge);
                final HashMap<String, String> map = this.parseServerResponse(serverResponse);
                if (!map.get("v").equals(this.encodeBase64(this.serverSignature))) {
                    throw new SaslException("Server signature was invalid.");
                }
                return challenge;
            }
            
            public boolean isComplete() {
                return this.step > 2;
            }
            
            public byte[] unwrap(final byte[] incoming, final int offset, final int len) throws SaslException {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
            
            public byte[] wrap(final byte[] outgoing, final int offset, final int len) throws SaslException {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
            
            public Object getNegotiatedProperty(final String propName) {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
            
            public void dispose() throws SaslException {
            }
            
            private byte[] computeClientFirstMessage() throws SaslException {
                final String userName = "n=" + this.prepUserName(this.credential.getUserName());
                this.rPrefix = this.generateRandomString();
                final String nonce = "r=" + this.rPrefix;
                this.clientFirstMessageBare = userName + "," + nonce;
                final String clientFirstMessage = "n,," + this.clientFirstMessageBare;
                return this.decodeUTF8(clientFirstMessage);
            }
            
            private byte[] computeClientFinalMessage(final byte[] challenge) throws SaslException {
                final String serverFirstMessage = this.encodeUTF8(challenge);
                final HashMap<String, String> map = this.parseServerResponse(serverFirstMessage);
                final String r = map.get("r");
                if (!r.startsWith(this.rPrefix)) {
                    throw new SaslException("Server sent an invalid nonce.");
                }
                final String s = map.get("s");
                final String i = map.get("i");
                final String channelBinding = "c=" + this.encodeBase64(this.decodeUTF8("n,,"));
                final String nonce = "r=" + r;
                final String clientFinalMessageWithoutProof = channelBinding + "," + nonce;
                final byte[] saltedPassword = this.Hi(NativeAuthenticationHelper.createHash(this.credential.getUserName(), this.credential.getPassword()), this.decodeBase64(s), Integer.parseInt(i));
                final byte[] clientKey = this.HMAC(saltedPassword, "Client Key");
                final byte[] storedKey = this.H(clientKey);
                final String authMessage = this.clientFirstMessageBare + "," + serverFirstMessage + "," + clientFinalMessageWithoutProof;
                final byte[] clientSignature = this.HMAC(storedKey, authMessage);
                final byte[] clientProof = this.XOR(clientKey, clientSignature);
                final byte[] serverKey = this.HMAC(saltedPassword, "Server Key");
                this.serverSignature = this.HMAC(serverKey, authMessage);
                final String proof = "p=" + this.encodeBase64(clientProof);
                final String clientFinalMessage = clientFinalMessageWithoutProof + "," + proof;
                return this.decodeUTF8(clientFinalMessage);
            }
            
            private byte[] decodeBase64(final String str) {
                return this.base64Codec.decode(str);
            }
            
            private byte[] decodeUTF8(final String str) throws SaslException {
                try {
                    return str.getBytes("UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    throw new SaslException("UTF-8 is not a supported encoding.", e);
                }
            }
            
            private String encodeBase64(final byte[] bytes) {
                return this.base64Codec.encode(bytes);
            }
            
            private String encodeUTF8(final byte[] bytes) throws SaslException {
                try {
                    return new String(bytes, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    throw new SaslException("UTF-8 is not a supported encoding.", e);
                }
            }
            
            private String generateRandomString() {
                final int comma = 44;
                final int low = 33;
                final int high = 126;
                final int range = 93;
                final Random random = new SecureRandom();
                final char[] text = new char[24];
                for (int i = 0; i < 24; ++i) {
                    int next;
                    for (next = random.nextInt(93) + 33; next == 44; next = random.nextInt(93) + 33) {}
                    text[i] = (char)next;
                }
                return new String(text);
            }
            
            private byte[] H(final byte[] data) throws SaslException {
                try {
                    return MessageDigest.getInstance("SHA-1").digest(data);
                }
                catch (NoSuchAlgorithmException e) {
                    throw new SaslException("SHA-1 could not be found.", e);
                }
            }
            
            private byte[] Hi(final byte[] password, final byte[] salt, final int iterations) throws SaslException {
                final PBEKeySpec spec = new PBEKeySpec(this.encodeUTF8(password).toCharArray(), salt, iterations, 160);
                SecretKeyFactory keyFactory;
                try {
                    keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                }
                catch (NoSuchAlgorithmException e) {
                    throw new SaslException("Unable to find PBKDF2WithHmacSHA1.", e);
                }
                try {
                    return keyFactory.generateSecret(spec).getEncoded();
                }
                catch (InvalidKeySpecException e2) {
                    throw new SaslException("Invalid key spec for PBKDC2WithHmacSHA1.", e2);
                }
            }
            
            private byte[] HMAC(final byte[] bytes, final String key) throws SaslException {
                final SecretKeySpec signingKey = new SecretKeySpec(bytes, "HmacSHA1");
                Mac mac;
                try {
                    mac = Mac.getInstance("HmacSHA1");
                }
                catch (NoSuchAlgorithmException e) {
                    throw new SaslException("Could not find HmacSHA1.", e);
                }
                try {
                    mac.init(signingKey);
                }
                catch (InvalidKeyException e2) {
                    throw new SaslException("Could not initialize mac.", e2);
                }
                return mac.doFinal(this.decodeUTF8(key));
            }
            
            private HashMap<String, String> parseServerResponse(final String response) {
                final HashMap<String, String> map = new HashMap<String, String>();
                final String[] arr$;
                final String[] pairs = arr$ = response.split(",");
                for (final String pair : arr$) {
                    final String[] parts = pair.split("=", 2);
                    map.put(parts[0], parts[1]);
                }
                return map;
            }
            
            private String prepUserName(final String userName) {
                return userName.replace("=", "=3D").replace(",", "=2D");
            }
            
            private byte[] XOR(final byte[] a, final byte[] b) {
                final byte[] result = new byte[a.length];
                for (int i = 0; i < a.length; ++i) {
                    result[i] = (byte)(a[i] ^ b[i]);
                }
                return result;
            }
        }
    }
    
    abstract class SaslAuthenticator extends Authenticator
    {
        SaslAuthenticator(final Mongo mongo, final MongoCredential credentials) {
            super(mongo, credentials);
        }
        
        public CommandResult authenticate() {
            final SaslClient saslClient = this.createSaslClient();
            try {
                byte[] response = (byte[])(saslClient.hasInitialResponse() ? saslClient.evaluateChallenge(new byte[0]) : null);
                CommandResult res = this.sendSaslStart(response);
                res.throwOnError();
                final int conversationId = (int)res.get("conversationId");
                while (!(boolean)res.get("done")) {
                    response = saslClient.evaluateChallenge((byte[])res.get("payload"));
                    if (response == null) {
                        throw new MongoException("SASL protocol error: no client response to challenge");
                    }
                    res = this.sendSaslContinue(conversationId, response);
                    res.throwOnError();
                }
                return res;
            }
            catch (IOException e) {
                throw new MongoException.Network("IOException authenticating the connection", e);
            }
            finally {
                try {
                    saslClient.dispose();
                }
                catch (SaslException ex) {}
            }
        }
        
        protected abstract SaslClient createSaslClient();
        
        protected DB getDatabase() {
            return this.mongo.getDB(this.credential.getSource());
        }
        
        private CommandResult sendSaslStart(final byte[] outToken) throws IOException {
            final DBObject cmd = new BasicDBObject("saslStart", 1).append("mechanism", this.getMechanismName()).append("payload", (outToken != null) ? outToken : new byte[0]);
            return DBPort.this.runCommand(this.getDatabase(), cmd);
        }
        
        private CommandResult sendSaslContinue(final int conversationId, final byte[] outToken) throws IOException {
            final DB adminDB = this.getDatabase();
            final DBObject cmd = new BasicDBObject("saslContinue", 1).append("conversationId", conversationId).append("payload", outToken);
            return DBPort.this.runCommand(adminDB, cmd);
        }
        
        public abstract String getMechanismName();
    }
    
    class X509Authenticator extends Authenticator
    {
        X509Authenticator(final Mongo mongo, final MongoCredential credential) {
            super(mongo, credential);
        }
        
        CommandResult authenticate() {
            try {
                final DB db = this.mongo.getDB(this.credential.getSource());
                final CommandResult res = DBPort.this.runCommand(db, this.getAuthCommand());
                res.throwOnError();
                return res;
            }
            catch (IOException e) {
                throw new MongoException.Network("IOException authenticating the connection", e);
            }
        }
        
        private DBObject getAuthCommand() {
            return new BasicDBObject("authenticate", 1).append("user", this.credential.getUserName()).append("mechanism", "MONGODB-X509");
        }
    }
    
    class NativeAuthenticator extends Authenticator
    {
        NativeAuthenticator(final Mongo mongo, final MongoCredential credentials) {
            super(mongo, credentials);
        }
        
        public CommandResult authenticate() {
            try {
                final DB db = this.mongo.getDB(this.credential.getSource());
                CommandResult res = DBPort.this.runCommand(db, NativeAuthenticationHelper.getNonceCommand());
                res.throwOnError();
                res = DBPort.this.runCommand(db, NativeAuthenticationHelper.getAuthCommand(this.credential.getUserName(), this.credential.getPassword(), res.getString("nonce")));
                res.throwOnError();
                return res;
            }
            catch (IOException e) {
                throw new MongoException.Network("IOException authenticating the connection", e);
            }
        }
    }
    
    abstract class Authenticator
    {
        protected final Mongo mongo;
        protected final MongoCredential credential;
        
        Authenticator(final Mongo mongo, final MongoCredential credential) {
            super();
            this.mongo = mongo;
            this.credential = credential;
        }
        
        abstract CommandResult authenticate();
    }
    
    interface Operation<T>
    {
        T execute() throws IOException;
    }
}
