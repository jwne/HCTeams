package com.mongodb;

import java.util.concurrent.atomic.*;
import org.bson.util.*;
import java.util.concurrent.*;
import java.util.*;
import java.net.*;
import java.io.*;

@Deprecated
public class DBTCPConnector implements DBConnector
{
    private static final AtomicInteger NEXT_CLUSTER_ID;
    private volatile boolean _closed;
    private final Mongo _mongo;
    private Cluster cluster;
    private final MyPort _myPort;
    private final ClusterConnectionMode connectionMode;
    private ClusterType type;
    private MongosHAServerSelector mongosHAServerSelector;
    
    public DBTCPConnector(final Mongo mongo) {
        super();
        this._myPort = new MyPort();
        this.type = ClusterType.Unknown;
        this._mongo = mongo;
        this.connectionMode = ((this._mongo.getAuthority().getType() == MongoAuthority.Type.Set || this._mongo.getMongoOptions().getRequiredReplicaSetName() != null) ? ClusterConnectionMode.Multiple : ClusterConnectionMode.Single);
    }
    
    public void start() {
        Assertions.isTrue("open", !this._closed);
        final MongoOptions options = this._mongo.getMongoOptions();
        final String clusterId = Integer.toString(DBTCPConnector.NEXT_CLUSTER_ID.getAndIncrement());
        this.cluster = Clusters.create(clusterId, ClusterSettings.builder().hosts(this._mongo.getAuthority().getServerAddresses()).mode(this.connectionMode).requiredReplicaSetName(this._mongo.getMongoOptions().getRequiredReplicaSetName()).build(), ServerSettings.builder().heartbeatFrequency(options.heartbeatFrequencyMS, TimeUnit.MILLISECONDS).heartbeatConnectRetryFrequency(options.heartbeatConnectRetryFrequencyMS, TimeUnit.MILLISECONDS).heartbeatSocketSettings(SocketSettings.builder().connectTimeout(options.heartbeatConnectTimeoutMS, TimeUnit.MILLISECONDS).readTimeout(options.heartbeatReadTimeoutMS, TimeUnit.MILLISECONDS).socketFactory(this._mongo.getMongoOptions().getSocketFactory()).build()).build(), null, this._mongo);
    }
    
    public void requestStart() {
        Assertions.isTrue("open", !this._closed);
        this._myPort.requestStart();
    }
    
    public void requestDone() {
        Assertions.isTrue("open", !this._closed);
        this._myPort.requestDone();
    }
    
    public void requestEnsureConnection() {
        Assertions.isTrue("open", !this._closed);
        this._myPort.requestEnsureConnection();
    }
    
    private WriteResult _checkWriteError(final DB db, final DBPort port, final WriteConcern concern) throws IOException {
        final CommandResult e = port.runCommand(db, concern.getCommand());
        e.throwOnError();
        return new WriteResult(e, concern);
    }
    
    public WriteResult say(final DB db, final OutMessage m, final WriteConcern concern) {
        Assertions.isTrue("open", !this._closed);
        return this.say(db, m, concern, (ServerAddress)null);
    }
    
    public WriteResult say(final DB db, final OutMessage m, final WriteConcern concern, final ServerAddress hostNeeded) {
        Assertions.isTrue("open", !this._closed);
        final DBPort port = this._myPort.get(true, ReadPreference.primary(), hostNeeded);
        try {
            return this.say(db, m, concern, port);
        }
        finally {
            this._myPort.done(port);
        }
    }
    
    WriteResult say(final DB db, final OutMessage m, final WriteConcern concern, final DBPort port) {
        Assertions.isTrue("open", !this._closed);
        if (concern == null) {
            throw new IllegalArgumentException("Write concern is null");
        }
        try {
            return this.doOperation(db, port, (DBPort.Operation<WriteResult>)new DBPort.Operation<WriteResult>() {
                public WriteResult execute() throws IOException {
                    port.say(m);
                    if (concern.callGetLastError()) {
                        return DBTCPConnector.this._checkWriteError(db, port, concern);
                    }
                    return new WriteResult(db, port, concern);
                }
            });
        }
        catch (MongoException.Network e) {
            if (concern.raiseNetworkErrors()) {
                throw e;
            }
            final CommandResult res = new CommandResult(port.serverAddress());
            res.put("ok", false);
            res.put("$err", "NETWORK ERROR");
            return new WriteResult(res, concern);
        }
        finally {
            m.doneWithMessage();
        }
    }
    
     <T> T doOperation(final DB db, final DBPort port, final DBPort.Operation<T> operation) {
        Assertions.isTrue("open", !this._closed);
        try {
            port.checkAuth(db.getMongo());
            return operation.execute();
        }
        catch (MongoException re) {
            throw re;
        }
        catch (IOException ioe) {
            this._myPort.error(port, ioe);
            throw new MongoException.Network("Operation on server " + port.getAddress() + " failed", ioe);
        }
        catch (RuntimeException re2) {
            this._myPort.error(port, re2);
            throw re2;
        }
    }
    
    public Response call(final DB db, final DBCollection coll, final OutMessage m, final ServerAddress hostNeeded, final DBDecoder decoder) {
        Assertions.isTrue("open", !this._closed);
        return this.call(db, coll, m, hostNeeded, 2, null, decoder);
    }
    
    public Response call(final DB db, final DBCollection coll, final OutMessage m, final ServerAddress hostNeeded, final int retries) {
        Assertions.isTrue("open", !this._closed);
        return this.call(db, coll, m, hostNeeded, retries, null, null);
    }
    
    public Response call(final DB db, final DBCollection coll, final OutMessage m, final ServerAddress hostNeeded, final int retries, final ReadPreference readPref, final DBDecoder decoder) {
        Assertions.isTrue("open", !this._closed);
        try {
            return this.innerCall(db, coll, m, hostNeeded, retries, readPref, decoder);
        }
        finally {
            m.doneWithMessage();
        }
    }
    
    private Response innerCall(final DB db, final DBCollection coll, final OutMessage m, final ServerAddress hostNeeded, final int remainingRetries, ReadPreference readPref, final DBDecoder decoder) {
        if (readPref == null) {
            readPref = ReadPreference.primary();
        }
        if (readPref == ReadPreference.primary() && m.hasOption(4)) {
            readPref = ReadPreference.secondaryPreferred();
        }
        final DBPort port = this._myPort.get(false, readPref, hostNeeded);
        Response res = null;
        boolean retry = false;
        try {
            port.checkAuth(db.getMongo());
            res = port.call(m, coll, decoder);
            if (res._responseTo != m.getId()) {
                throw new MongoException("ids don't match");
            }
        }
        catch (IOException ioe) {
            this._myPort.error(port, ioe);
            retry = this.shouldRetryQuery(readPref, coll, ioe, remainingRetries);
            if (!retry) {
                throw new MongoException.Network("Read operation to server " + port.host() + " failed on database " + db, ioe);
            }
        }
        catch (RuntimeException re) {
            this._myPort.error(port, re);
            throw re;
        }
        finally {
            this._myPort.done(port);
        }
        if (retry) {
            return this.innerCall(db, coll, m, hostNeeded, remainingRetries - 1, readPref, decoder);
        }
        final ServerError err = res.getError();
        if (err == null || !err.isNotMasterError()) {
            return res;
        }
        if (remainingRetries <= 0) {
            throw new MongoException("not talking to master and retries used up");
        }
        return this.innerCall(db, coll, m, hostNeeded, remainingRetries - 1, readPref, decoder);
    }
    
    public ServerAddress getAddress() {
        Assertions.isTrue("open", !this._closed);
        final ClusterDescription clusterDescription = this.getClusterDescription();
        if (this.connectionMode == ClusterConnectionMode.Single) {
            return clusterDescription.getAny().get(0).getAddress();
        }
        if (clusterDescription.getPrimaries().isEmpty()) {
            return null;
        }
        return clusterDescription.getPrimaries().get(0).getAddress();
    }
    
    public List<ServerAddress> getAllAddress() {
        Assertions.isTrue("open", !this._closed);
        return this._mongo._authority.getServerAddresses();
    }
    
    public List<ServerAddress> getServerAddressList() {
        Assertions.isTrue("open", !this._closed);
        final List<ServerAddress> serverAddressList = new ArrayList<ServerAddress>();
        final ClusterDescription clusterDescription = this.getClusterDescription();
        for (final ServerDescription serverDescription : clusterDescription.getAll()) {
            serverAddressList.add(serverDescription.getAddress());
        }
        return serverAddressList;
    }
    
    public ReplicaSetStatus getReplicaSetStatus() {
        Assertions.isTrue("open", !this._closed);
        return (this.getType() == ClusterType.ReplicaSet && this.connectionMode == ClusterConnectionMode.Multiple) ? new ReplicaSetStatus(this.getClusterDescription()) : null;
    }
    
    boolean isMongosConnection() {
        Assertions.isTrue("open", !this._closed);
        return this.getType() == ClusterType.Sharded;
    }
    
    public String getConnectPoint() {
        Assertions.isTrue("open", !this._closed);
        final ServerAddress master = this.getAddress();
        return (master != null) ? master.toString() : null;
    }
    
    private boolean shouldRetryQuery(final ReadPreference readPreference, final DBCollection coll, final IOException ioe, final int remainingRetries) {
        return remainingRetries != 0 && !coll._name.equals("$cmd") && !(ioe instanceof SocketTimeoutException) && !readPreference.equals(ReadPreference.primary()) && this.connectionMode == ClusterConnectionMode.Multiple && this.getType() == ClusterType.ReplicaSet;
    }
    
    private ClusterDescription getClusterDescription() {
        return this.cluster.getDescription(this.getClusterWaitTimeMS(), TimeUnit.MILLISECONDS);
    }
    
    private int getClusterWaitTimeMS() {
        return Math.min(this._mongo.getMongoOptions().maxWaitTime, this._mongo.getMongoOptions().connectTimeout);
    }
    
    private int getConnectionWaitTimeMS() {
        return this._mongo.getMongoOptions().maxWaitTime;
    }
    
    DBPort getPrimaryPort() {
        Assertions.isTrue("open", !this._closed);
        return this._myPort.get(true, ReadPreference.primary(), null);
    }
    
    void releasePort(final DBPort port) {
        Assertions.isTrue("open", !this._closed);
        this._myPort.done(port);
    }
    
    ServerDescription getServerDescription(final ServerAddress address) {
        Assertions.isTrue("open", !this._closed);
        return this.getClusterDescription().getByServerAddress(address);
    }
    
    private ServerSelector createServerSelector(final ReadPreference readPreference) {
        if (this.connectionMode == ClusterConnectionMode.Multiple) {
            final List<ServerSelector> serverSelectorList = new ArrayList<ServerSelector>();
            if (this.getType() == ClusterType.Sharded) {
                serverSelectorList.add(this.getMongosHAServerSelector());
            }
            else if (this.getType() == ClusterType.ReplicaSet) {
                serverSelectorList.add(new ReadPreferenceServerSelector(readPreference));
            }
            else {
                serverSelectorList.add(new AnyServerSelector());
            }
            serverSelectorList.add(new LatencyMinimizingServerSelector(this._mongo.getMongoOptions().acceptableLatencyDifferenceMS, TimeUnit.MILLISECONDS));
            return new CompositeServerSelector(serverSelectorList);
        }
        return new AnyServerSelector();
    }
    
    private synchronized ClusterType getType() {
        if (this.type == ClusterType.Unknown) {
            this.type = this.getClusterDescription().getType();
        }
        return this.type;
    }
    
    private synchronized MongosHAServerSelector getMongosHAServerSelector() {
        if (this.mongosHAServerSelector == null) {
            this.mongosHAServerSelector = new MongosHAServerSelector();
        }
        return this.mongosHAServerSelector;
    }
    
    public String debugString() {
        return this.getClusterDescription().getShortDescription();
    }
    
    public void close() {
        this._closed = true;
        if (this.cluster != null) {
            this.cluster.close();
            this.cluster = null;
        }
    }
    
    public void updatePortPool(final ServerAddress addr) {
    }
    
    public DBPortPool getDBPortPool(final ServerAddress addr) {
        throw new UnsupportedOperationException();
    }
    
    public boolean isOpen() {
        return !this._closed;
    }
    
    public CommandResult authenticate(final MongoCredential credentials) {
        final DBPort port = this._myPort.get(false, ReadPreference.primaryPreferred(), null);
        try {
            final CommandResult result = port.authenticate(this._mongo, credentials);
            this._mongo.getAuthority().getCredentialsStore().add(credentials);
            return result;
        }
        finally {
            this._myPort.done(port);
        }
    }
    
    public int getMaxBsonObjectSize() {
        final ClusterDescription clusterDescription = this.getClusterDescription();
        if (clusterDescription.getPrimaries().isEmpty()) {
            return 4194304;
        }
        return clusterDescription.getPrimaries().get(0).getMaxDocumentSize();
    }
    
    MyPort getMyPort() {
        return this._myPort;
    }
    
    private Server getServer(final ServerSelector serverSelector) {
        return this.cluster.getServer(serverSelector, this.getClusterWaitTimeMS(), TimeUnit.MILLISECONDS);
    }
    
    static {
        NEXT_CLUSTER_ID = new AtomicInteger(1);
    }
    
    class MyPort
    {
        private final ThreadLocal<PinnedRequestStatus> pinnedRequestStatusThreadLocal;
        
        MyPort() {
            super();
            this.pinnedRequestStatusThreadLocal = new ThreadLocal<PinnedRequestStatus>();
        }
        
        DBPort get(final boolean keep, final ReadPreference readPref, final ServerAddress hostNeeded) {
            final DBPort pinnedRequestPort = this.getPinnedRequestPortForThread();
            if (hostNeeded == null) {
                if (pinnedRequestPort != null) {
                    if (this.portIsAPrimary(pinnedRequestPort) || !keep) {
                        return pinnedRequestPort;
                    }
                    pinnedRequestPort.getProvider().release(pinnedRequestPort);
                    this.setPinnedRequestPortForThread(null);
                }
                final DBPort port = this.getConnection(DBTCPConnector.this.createServerSelector(readPref));
                if (this.threadHasPinnedRequest()) {
                    this.setPinnedRequestPortForThread(port);
                }
                return port;
            }
            if (pinnedRequestPort != null && pinnedRequestPort.serverAddress().equals(hostNeeded)) {
                return pinnedRequestPort;
            }
            return this.getConnection(new ServerAddressSelector(hostNeeded));
        }
        
        private boolean portIsAPrimary(final DBPort pinnedRequestPort) {
            for (final ServerDescription cur : DBTCPConnector.this.getClusterDescription().getPrimaries()) {
                if (cur.getAddress().equals(pinnedRequestPort.serverAddress())) {
                    return true;
                }
            }
            return false;
        }
        
        void done(final DBPort port) {
            final Connection requestPort = this.getPinnedRequestPortForThread();
            if (port != requestPort) {
                port.getProvider().release(port);
            }
        }
        
        void error(final DBPort port, final Exception e) {
            if (!(e instanceof InterruptedIOException)) {
                DBTCPConnector.this.getServer(new ServerAddressSelector(port.getAddress())).invalidate();
            }
            port.close();
            this.pinnedRequestStatusThreadLocal.remove();
        }
        
        void requestEnsureConnection() {
            if (!this.threadHasPinnedRequest()) {
                return;
            }
            if (this.getPinnedRequestPortForThread() != null) {
                return;
            }
            this.setPinnedRequestPortForThread(this.getConnection(DBTCPConnector.this.createServerSelector(ReadPreference.primary())));
        }
        
        private DBPort getConnection(final ServerSelector serverSelector) {
            return (DBPort)DBTCPConnector.this.getServer(serverSelector).getConnection(DBTCPConnector.this.getConnectionWaitTimeMS(), TimeUnit.MILLISECONDS);
        }
        
        void requestStart() {
            final PinnedRequestStatus current = this.getPinnedRequestStatusForThread();
            if (current == null) {
                this.pinnedRequestStatusThreadLocal.set(new PinnedRequestStatus());
            }
            else {
                final PinnedRequestStatus pinnedRequestStatus = current;
                ++pinnedRequestStatus.nestedBindings;
            }
        }
        
        void requestDone() {
            final PinnedRequestStatus current = this.getPinnedRequestStatusForThread();
            if (current != null) {
                if (current.nestedBindings > 0) {
                    final PinnedRequestStatus pinnedRequestStatus = current;
                    --pinnedRequestStatus.nestedBindings;
                }
                else {
                    this.pinnedRequestStatusThreadLocal.remove();
                    if (current.requestPort != null) {
                        current.requestPort.getProvider().release(current.requestPort);
                    }
                }
            }
        }
        
        PinnedRequestStatus getPinnedRequestStatusForThread() {
            return this.pinnedRequestStatusThreadLocal.get();
        }
        
        boolean threadHasPinnedRequest() {
            return this.pinnedRequestStatusThreadLocal.get() != null;
        }
        
        DBPort getPinnedRequestPortForThread() {
            return this.threadHasPinnedRequest() ? this.pinnedRequestStatusThreadLocal.get().requestPort : null;
        }
        
        void setPinnedRequestPortForThread(final DBPort port) {
            this.pinnedRequestStatusThreadLocal.get().requestPort = port;
        }
    }
    
    static class PinnedRequestStatus
    {
        DBPort requestPort;
        public int nestedBindings;
    }
}
