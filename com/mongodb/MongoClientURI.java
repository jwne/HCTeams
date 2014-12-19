package com.mongodb;

import java.util.logging.*;
import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import java.util.*;

public class MongoClientURI
{
    private static final String PREFIX = "mongodb://";
    private static final String UTF_8 = "UTF-8";
    static Set<String> generalOptionsKeys;
    static Set<String> authKeys;
    static Set<String> readPreferenceKeys;
    static Set<String> writeConcernKeys;
    static Set<String> allKeys;
    private final MongoClientOptions options;
    private final MongoCredential credentials;
    private final List<String> hosts;
    private final String database;
    private final String collection;
    private final String uri;
    static final Logger LOGGER;
    
    public MongoClientURI(final String uri) {
        this(uri, new MongoClientOptions.Builder());
    }
    
    public MongoClientURI(String uri, final MongoClientOptions.Builder builder) {
        super();
        try {
            this.uri = uri;
            if (!uri.startsWith("mongodb://")) {
                throw new IllegalArgumentException("uri needs to start with mongodb://");
            }
            uri = uri.substring("mongodb://".length());
            String userName = null;
            char[] password = null;
            int idx = uri.lastIndexOf("/");
            String serverPart;
            String nsPart;
            String optionsPart;
            if (idx < 0) {
                if (uri.contains("?")) {
                    throw new IllegalArgumentException("URI contains options without trailing slash");
                }
                serverPart = uri;
                nsPart = null;
                optionsPart = "";
            }
            else {
                serverPart = uri.substring(0, idx);
                nsPart = uri.substring(idx + 1);
                idx = nsPart.indexOf("?");
                if (idx >= 0) {
                    optionsPart = nsPart.substring(idx + 1);
                    nsPart = nsPart.substring(0, idx);
                }
                else {
                    optionsPart = "";
                }
            }
            final List<String> all = new LinkedList<String>();
            int idx2 = serverPart.indexOf("@");
            if (idx2 > 0) {
                final String authPart = serverPart.substring(0, idx2);
                serverPart = serverPart.substring(idx2 + 1);
                idx2 = authPart.indexOf(":");
                if (idx2 == -1) {
                    userName = URLDecoder.decode(authPart, "UTF-8");
                }
                else {
                    userName = URLDecoder.decode(authPart.substring(0, idx2), "UTF-8");
                    password = URLDecoder.decode(authPart.substring(idx2 + 1), "UTF-8").toCharArray();
                }
            }
            Collections.addAll(all, serverPart.split(","));
            Collections.sort(all);
            this.hosts = Collections.unmodifiableList((List<? extends String>)all);
            if (nsPart != null && nsPart.length() != 0) {
                idx = nsPart.indexOf(".");
                if (idx < 0) {
                    this.database = nsPart;
                    this.collection = null;
                }
                else {
                    this.database = nsPart.substring(0, idx);
                    this.collection = nsPart.substring(idx + 1);
                }
            }
            else {
                this.database = null;
                this.collection = null;
            }
            final Map<String, List<String>> optionsMap = this.parseOptions(optionsPart);
            this.options = this.createOptions(optionsMap, builder);
            this.credentials = this.createCredentials(optionsMap, userName, password, this.database);
            this.warnOnUnsupportedOptions(optionsMap);
        }
        catch (UnsupportedEncodingException e) {
            throw new MongoInternalException("This should not happen", e);
        }
    }
    
    private void warnOnUnsupportedOptions(final Map<String, List<String>> optionsMap) {
        for (final String key : optionsMap.keySet()) {
            if (!MongoClientURI.allKeys.contains(key)) {
                MongoClientURI.LOGGER.warning("Unknown or Unsupported Option '" + key + "'");
            }
        }
    }
    
    private MongoClientOptions createOptions(final Map<String, List<String>> optionsMap, final MongoClientOptions.Builder builder) {
        for (final String key : MongoClientURI.generalOptionsKeys) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            if (key.equals("maxpoolsize")) {
                builder.connectionsPerHost(Integer.parseInt(value));
            }
            else if (key.equals("minpoolsize")) {
                builder.minConnectionsPerHost(Integer.parseInt(value));
            }
            else if (key.equals("maxidletimems")) {
                builder.maxConnectionIdleTime(Integer.parseInt(value));
            }
            else if (key.equals("maxlifetimems")) {
                builder.maxConnectionLifeTime(Integer.parseInt(value));
            }
            else if (key.equals("waitqueuemultiple")) {
                builder.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(value));
            }
            else if (key.equals("waitqueuetimeoutms")) {
                builder.maxWaitTime(Integer.parseInt(value));
            }
            else if (key.equals("connecttimeoutms")) {
                builder.connectTimeout(Integer.parseInt(value));
            }
            else if (key.equals("sockettimeoutms")) {
                builder.socketTimeout(Integer.parseInt(value));
            }
            else if (key.equals("autoconnectretry")) {
                builder.autoConnectRetry(this._parseBoolean(value));
            }
            else if (key.equals("replicaset")) {
                builder.requiredReplicaSetName(value);
            }
            else {
                if (!key.equals("ssl") || !this._parseBoolean(value)) {
                    continue;
                }
                builder.socketFactory(SSLSocketFactory.getDefault());
            }
        }
        final WriteConcern writeConcern = this.createWriteConcern(optionsMap);
        final ReadPreference readPreference = this.createReadPreference(optionsMap);
        if (writeConcern != null) {
            builder.writeConcern(writeConcern);
        }
        if (readPreference != null) {
            builder.readPreference(readPreference);
        }
        return builder.build();
    }
    
    private WriteConcern createWriteConcern(final Map<String, List<String>> optionsMap) {
        Boolean safe = null;
        String w = null;
        int wTimeout = 0;
        boolean fsync = false;
        boolean journal = false;
        for (final String key : MongoClientURI.writeConcernKeys) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            if (key.equals("safe")) {
                safe = this._parseBoolean(value);
            }
            else if (key.equals("w")) {
                w = value;
            }
            else if (key.equals("wtimeoutms")) {
                wTimeout = Integer.parseInt(value);
            }
            else if (key.equals("fsync")) {
                fsync = this._parseBoolean(value);
            }
            else {
                if (!key.equals("j")) {
                    continue;
                }
                journal = this._parseBoolean(value);
            }
        }
        return this.buildWriteConcern(safe, w, wTimeout, fsync, journal);
    }
    
    private ReadPreference createReadPreference(final Map<String, List<String>> optionsMap) {
        Boolean slaveOk = null;
        String readPreferenceType = null;
        DBObject firstTagSet = null;
        final List<DBObject> remainingTagSets = new ArrayList<DBObject>();
        for (final String key : MongoClientURI.readPreferenceKeys) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            if (key.equals("slaveok")) {
                slaveOk = this._parseBoolean(value);
            }
            else if (key.equals("readpreference")) {
                readPreferenceType = value;
            }
            else {
                if (!key.equals("readpreferencetags")) {
                    continue;
                }
                for (final String cur : optionsMap.get(key)) {
                    final DBObject tagSet = this.getTagSet(cur.trim());
                    if (firstTagSet == null) {
                        firstTagSet = tagSet;
                    }
                    else {
                        remainingTagSets.add(tagSet);
                    }
                }
            }
        }
        return this.buildReadPreference(readPreferenceType, firstTagSet, remainingTagSets, slaveOk);
    }
    
    private MongoCredential createCredentials(final Map<String, List<String>> optionsMap, final String userName, final char[] password, String database) {
        if (userName == null) {
            return null;
        }
        if (database == null) {
            database = "admin";
        }
        String mechanism = null;
        String authSource = database;
        String gssapiServiceName = null;
        for (final String key : MongoClientURI.authKeys) {
            final String value = this.getLastValue(optionsMap, key);
            if (value == null) {
                continue;
            }
            if (key.equals("authmechanism")) {
                mechanism = value;
            }
            else if (key.equals("authsource")) {
                authSource = value;
            }
            else {
                if (!key.equals("gssapiservicename")) {
                    continue;
                }
                gssapiServiceName = value;
            }
        }
        if ("GSSAPI".equals(mechanism)) {
            MongoCredential gssapiCredential = MongoCredential.createGSSAPICredential(userName);
            if (gssapiServiceName != null) {
                gssapiCredential = gssapiCredential.withMechanismProperty("SERVICE_NAME", gssapiServiceName);
            }
            return gssapiCredential;
        }
        if ("PLAIN".equals(mechanism)) {
            return MongoCredential.createPlainCredential(userName, authSource, password);
        }
        if ("MONGODB-CR".equals(mechanism)) {
            return MongoCredential.createMongoCRCredential(userName, authSource, password);
        }
        if ("MONGODB-X509".equals(mechanism)) {
            return MongoCredential.createMongoX509Credential(userName);
        }
        if ("SCRAM-SHA-1".equals(mechanism)) {
            return MongoCredential.createScramSha1Credential(userName, authSource, password);
        }
        if (mechanism == null) {
            return MongoCredential.createCredential(userName, authSource, password);
        }
        throw new IllegalArgumentException("Unsupported authMechanism: " + mechanism);
    }
    
    private String getLastValue(final Map<String, List<String>> optionsMap, final String key) {
        final List<String> valueList = optionsMap.get(key);
        if (valueList == null) {
            return null;
        }
        return valueList.get(valueList.size() - 1);
    }
    
    private Map<String, List<String>> parseOptions(final String optionsPart) {
        final Map<String, List<String>> optionsMap = new HashMap<String, List<String>>();
        for (final String _part : optionsPart.split("&|;")) {
            final int idx = _part.indexOf("=");
            if (idx >= 0) {
                final String key = _part.substring(0, idx).toLowerCase();
                final String value = _part.substring(idx + 1);
                List<String> valueList = optionsMap.get(key);
                if (valueList == null) {
                    valueList = new ArrayList<String>(1);
                }
                valueList.add(value);
                optionsMap.put(key, valueList);
            }
        }
        if (optionsMap.containsKey("wtimeout") && !optionsMap.containsKey("wtimeoutms")) {
            optionsMap.put("wtimeoutms", optionsMap.remove("wtimeout"));
        }
        return optionsMap;
    }
    
    private ReadPreference buildReadPreference(final String readPreferenceType, final DBObject firstTagSet, final List<DBObject> remainingTagSets, final Boolean slaveOk) {
        if (readPreferenceType != null) {
            if (firstTagSet == null) {
                return ReadPreference.valueOf(readPreferenceType);
            }
            return ReadPreference.valueOf(readPreferenceType, firstTagSet, (DBObject[])remainingTagSets.toArray(new DBObject[remainingTagSets.size()]));
        }
        else {
            if (slaveOk != null && slaveOk.equals(Boolean.TRUE)) {
                return ReadPreference.secondaryPreferred();
            }
            return null;
        }
    }
    
    private WriteConcern buildWriteConcern(final Boolean safe, final String w, final int wTimeout, final boolean fsync, final boolean journal) {
        if (w != null || wTimeout != 0 || fsync || journal) {
            if (w == null) {
                return new WriteConcern(1, wTimeout, fsync, journal);
            }
            try {
                return new WriteConcern(Integer.parseInt(w), wTimeout, fsync, journal);
            }
            catch (NumberFormatException e) {
                return new WriteConcern(w, wTimeout, fsync, journal);
            }
        }
        if (safe == null) {
            return null;
        }
        if (safe) {
            return WriteConcern.ACKNOWLEDGED;
        }
        return WriteConcern.UNACKNOWLEDGED;
    }
    
    private DBObject getTagSet(final String tagSetString) {
        final DBObject tagSet = new BasicDBObject();
        if (tagSetString.length() > 0) {
            for (final String tag : tagSetString.split(",")) {
                final String[] tagKeyValuePair = tag.split(":");
                if (tagKeyValuePair.length != 2) {
                    throw new IllegalArgumentException("Bad read preference tags: " + tagSetString);
                }
                tagSet.put(tagKeyValuePair[0].trim(), tagKeyValuePair[1].trim());
            }
        }
        return tagSet;
    }
    
    boolean _parseBoolean(final String _in) {
        final String in = _in.trim();
        return in != null && in.length() > 0 && (in.equals("1") || in.toLowerCase().equals("true") || in.toLowerCase().equals("yes"));
    }
    
    public String getUsername() {
        return (this.credentials != null) ? this.credentials.getUserName() : null;
    }
    
    public char[] getPassword() {
        return (char[])((this.credentials != null) ? this.credentials.getPassword() : null);
    }
    
    public List<String> getHosts() {
        return this.hosts;
    }
    
    public String getDatabase() {
        return this.database;
    }
    
    public String getCollection() {
        return this.collection;
    }
    
    public String getURI() {
        return this.uri;
    }
    
    public MongoCredential getCredentials() {
        return this.credentials;
    }
    
    public MongoClientOptions getOptions() {
        return this.options;
    }
    
    public String toString() {
        return this.uri;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoClientURI that = (MongoClientURI)o;
        if (!this.hosts.equals(that.hosts)) {
            return false;
        }
        Label_0080: {
            if (this.database != null) {
                if (this.database.equals(that.database)) {
                    break Label_0080;
                }
            }
            else if (that.database == null) {
                break Label_0080;
            }
            return false;
        }
        Label_0113: {
            if (this.collection != null) {
                if (this.collection.equals(that.collection)) {
                    break Label_0113;
                }
            }
            else if (that.collection == null) {
                break Label_0113;
            }
            return false;
        }
        if (this.credentials != null) {
            if (this.credentials.equals(that.credentials)) {
                return this.options.equals(that.options);
            }
        }
        else if (that.credentials == null) {
            return this.options.equals(that.options);
        }
        return false;
    }
    
    public int hashCode() {
        int result = this.options.hashCode();
        result = 31 * result + ((this.credentials != null) ? this.credentials.hashCode() : 0);
        result = 31 * result + this.hosts.hashCode();
        result = 31 * result + ((this.database != null) ? this.database.hashCode() : 0);
        result = 31 * result + ((this.collection != null) ? this.collection.hashCode() : 0);
        return result;
    }
    
    static {
        MongoClientURI.generalOptionsKeys = new HashSet<String>();
        MongoClientURI.authKeys = new HashSet<String>();
        MongoClientURI.readPreferenceKeys = new HashSet<String>();
        MongoClientURI.writeConcernKeys = new HashSet<String>();
        MongoClientURI.allKeys = new HashSet<String>();
        MongoClientURI.generalOptionsKeys.add("minpoolsize");
        MongoClientURI.generalOptionsKeys.add("maxpoolsize");
        MongoClientURI.generalOptionsKeys.add("waitqueuemultiple");
        MongoClientURI.generalOptionsKeys.add("waitqueuetimeoutms");
        MongoClientURI.generalOptionsKeys.add("connecttimeoutms");
        MongoClientURI.generalOptionsKeys.add("maxidletimems");
        MongoClientURI.generalOptionsKeys.add("maxlifetimems");
        MongoClientURI.generalOptionsKeys.add("sockettimeoutms");
        MongoClientURI.generalOptionsKeys.add("sockettimeoutms");
        MongoClientURI.generalOptionsKeys.add("autoconnectretry");
        MongoClientURI.generalOptionsKeys.add("ssl");
        MongoClientURI.generalOptionsKeys.add("replicaset");
        MongoClientURI.readPreferenceKeys.add("slaveok");
        MongoClientURI.readPreferenceKeys.add("readpreference");
        MongoClientURI.readPreferenceKeys.add("readpreferencetags");
        MongoClientURI.writeConcernKeys.add("safe");
        MongoClientURI.writeConcernKeys.add("w");
        MongoClientURI.writeConcernKeys.add("wtimeoutms");
        MongoClientURI.writeConcernKeys.add("fsync");
        MongoClientURI.writeConcernKeys.add("j");
        MongoClientURI.authKeys.add("authmechanism");
        MongoClientURI.authKeys.add("authsource");
        MongoClientURI.authKeys.add("gssapiservicename");
        MongoClientURI.allKeys.addAll(MongoClientURI.generalOptionsKeys);
        MongoClientURI.allKeys.addAll(MongoClientURI.authKeys);
        MongoClientURI.allKeys.addAll(MongoClientURI.readPreferenceKeys);
        MongoClientURI.allKeys.addAll(MongoClientURI.writeConcernKeys);
        LOGGER = Logger.getLogger("com.mongodb.MongoURI");
    }
}
