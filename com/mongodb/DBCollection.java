package com.mongodb;

import java.util.concurrent.*;
import org.bson.types.*;
import java.util.*;
import java.io.*;

public abstract class DBCollection
{
    final DB _db;
    @Deprecated
    protected final String _name;
    @Deprecated
    protected final String _fullName;
    @Deprecated
    protected List<DBObject> _hintFields;
    private WriteConcern _concern;
    private ReadPreference _readPref;
    private DBDecoderFactory _decoderFactory;
    private DBEncoderFactory _encoderFactory;
    final Bytes.OptionHolder _options;
    @Deprecated
    protected Class _objectClass;
    private Map<String, Class> _internalClass;
    private ReflectionDBObject.JavaWrapper _wrapper;
    @Deprecated
    private final Set<String> _createdIndexes;
    
    public WriteResult insert(final DBObject[] arr, final WriteConcern concern) {
        return this.insert(arr, concern, this.getDBEncoder());
    }
    
    public WriteResult insert(final DBObject[] arr, final WriteConcern concern, final DBEncoder encoder) {
        return this.insert(Arrays.asList(arr), concern, encoder);
    }
    
    public WriteResult insert(final DBObject o, final WriteConcern concern) {
        return this.insert(Arrays.asList(o), concern);
    }
    
    public WriteResult insert(final DBObject... arr) {
        return this.insert(arr, this.getWriteConcern());
    }
    
    public WriteResult insert(final WriteConcern concern, final DBObject... arr) {
        return this.insert(arr, concern);
    }
    
    public WriteResult insert(final List<DBObject> list) {
        return this.insert(list, this.getWriteConcern());
    }
    
    public WriteResult insert(final List<DBObject> list, final WriteConcern concern) {
        return this.insert(list, concern, this.getDBEncoder());
    }
    
    public abstract WriteResult insert(final List<DBObject> p0, final WriteConcern p1, final DBEncoder p2);
    
    public WriteResult insert(final List<DBObject> documents, final InsertOptions insertOptions) {
        WriteConcern writeConcern = (insertOptions.getWriteConcern() != null) ? insertOptions.getWriteConcern() : this.getWriteConcern();
        if (insertOptions.isContinueOnError()) {
            writeConcern = writeConcern.continueOnError(true);
        }
        final DBEncoder dbEncoder = (insertOptions.getDbEncoder() != null) ? insertOptions.getDbEncoder() : this.getDBEncoder();
        return this.insert(documents, writeConcern, dbEncoder);
    }
    
    public WriteResult update(final DBObject q, final DBObject o, final boolean upsert, final boolean multi, final WriteConcern concern) {
        return this.update(q, o, upsert, multi, concern, this.getDBEncoder());
    }
    
    public abstract WriteResult update(final DBObject p0, final DBObject p1, final boolean p2, final boolean p3, final WriteConcern p4, final DBEncoder p5);
    
    public WriteResult update(final DBObject q, final DBObject o, final boolean upsert, final boolean multi) {
        return this.update(q, o, upsert, multi, this.getWriteConcern());
    }
    
    public WriteResult update(final DBObject q, final DBObject o) {
        return this.update(q, o, false, false);
    }
    
    public WriteResult updateMulti(final DBObject q, final DBObject o) {
        return this.update(q, o, false, true);
    }
    
    @Deprecated
    protected abstract void doapply(final DBObject p0);
    
    public WriteResult remove(final DBObject o, final WriteConcern concern) {
        return this.remove(o, concern, this.getDBEncoder());
    }
    
    public abstract WriteResult remove(final DBObject p0, final WriteConcern p1, final DBEncoder p2);
    
    public WriteResult remove(final DBObject o) {
        return this.remove(o, this.getWriteConcern());
    }
    
    abstract QueryResultIterator find(final DBObject p0, final DBObject p1, final int p2, final int p3, final int p4, final int p5, final ReadPreference p6, final DBDecoder p7);
    
    abstract QueryResultIterator find(final DBObject p0, final DBObject p1, final int p2, final int p3, final int p4, final int p5, final ReadPreference p6, final DBDecoder p7, final DBEncoder p8);
    
    @Deprecated
    public DBCursor find(final DBObject query, final DBObject fields, final int numToSkip, final int batchSize, final int options) {
        return this.find(query, fields, numToSkip, batchSize).addOption(options);
    }
    
    @Deprecated
    public DBCursor find(final DBObject query, final DBObject fields, final int numToSkip, final int batchSize) {
        final DBCursor cursor = this.find(query, fields).skip(numToSkip).batchSize(batchSize);
        return cursor;
    }
    
    public DBObject findOne(final Object id) {
        return this.findOne(id, null);
    }
    
    public DBObject findOne(final Object id, final DBObject projection) {
        return this.findOne(new BasicDBObject("_id", id), projection);
    }
    
    public DBObject findAndModify(final DBObject query, final DBObject fields, final DBObject sort, final boolean remove, final DBObject update, final boolean returnNew, final boolean upsert) {
        return this.findAndModify(query, fields, sort, remove, update, returnNew, upsert, 0L, TimeUnit.MILLISECONDS);
    }
    
    public DBObject findAndModify(final DBObject query, final DBObject fields, final DBObject sort, final boolean remove, final DBObject update, final boolean returnNew, final boolean upsert, final long maxTime, final TimeUnit maxTimeUnit) {
        final BasicDBObject cmd = new BasicDBObject("findandmodify", this._name);
        if (query != null && !query.keySet().isEmpty()) {
            cmd.append("query", query);
        }
        if (fields != null && !fields.keySet().isEmpty()) {
            cmd.append("fields", fields);
        }
        if (sort != null && !sort.keySet().isEmpty()) {
            cmd.append("sort", sort);
        }
        if (maxTime > 0L) {
            cmd.append("maxTimeMS", TimeUnit.MILLISECONDS.convert(maxTime, maxTimeUnit));
        }
        if (remove) {
            cmd.append("remove", remove);
        }
        else {
            if (update != null && !update.keySet().isEmpty()) {
                final String key = update.keySet().iterator().next();
                if (key.charAt(0) != '$') {
                    this._checkObject(update, false, false);
                }
                cmd.append("update", update);
            }
            if (returnNew) {
                cmd.append("new", returnNew);
            }
            if (upsert) {
                cmd.append("upsert", upsert);
            }
        }
        if (remove && update != null && !update.keySet().isEmpty() && !returnNew) {
            throw new MongoException("FindAndModify: Remove cannot be mixed with the Update, or returnNew params!");
        }
        final CommandResult res = this._db.command(cmd);
        if (res.ok() || res.getErrorMessage().equals("No matching object found")) {
            return this.replaceWithObjectClass((DBObject)res.get("value"));
        }
        res.throwOnError();
        return null;
    }
    
    private DBObject replaceWithObjectClass(final DBObject oldObj) {
        if (oldObj == null || (this.getObjectClass() == null & this._internalClass.isEmpty())) {
            return oldObj;
        }
        final DBObject newObj = this.instantiateObjectClassInstance();
        for (final String key : oldObj.keySet()) {
            newObj.put(key, oldObj.get(key));
        }
        return newObj;
    }
    
    private DBObject instantiateObjectClassInstance() {
        try {
            return this.getObjectClass().newInstance();
        }
        catch (InstantiationException e) {
            throw new MongoInternalException("can't create instance of type " + this.getObjectClass(), e);
        }
        catch (IllegalAccessException e2) {
            throw new MongoInternalException("can't create instance of type " + this.getObjectClass(), e2);
        }
    }
    
    public DBObject findAndModify(final DBObject query, final DBObject sort, final DBObject update) {
        return this.findAndModify(query, null, sort, false, update, false, false);
    }
    
    public DBObject findAndModify(final DBObject query, final DBObject update) {
        return this.findAndModify(query, null, null, false, update, false, false);
    }
    
    public DBObject findAndRemove(final DBObject query) {
        return this.findAndModify(query, null, null, true, null, false, false);
    }
    
    public void createIndex(final String name) {
        this.createIndex(new BasicDBObject(name, 1));
    }
    
    public void createIndex(final DBObject keys) {
        this.createIndex(keys, this.defaultOptions(keys));
    }
    
    public void createIndex(final DBObject keys, final DBObject options) {
        this.createIndex(keys, options, this.getDBEncoder());
    }
    
    public void createIndex(final DBObject keys, final String name) {
        this.createIndex(keys, name, false);
    }
    
    public void createIndex(final DBObject keys, final String name, final boolean unique) {
        final DBObject options = this.defaultOptions(keys);
        if (name != null && name.length() > 0) {
            options.put("name", name);
        }
        if (unique) {
            options.put("unique", Boolean.TRUE);
        }
        this.createIndex(keys, options);
    }
    
    @Deprecated
    public abstract void createIndex(final DBObject p0, final DBObject p1, final DBEncoder p2);
    
    @Deprecated
    public void ensureIndex(final String name) {
        this.ensureIndex(new BasicDBObject(name, 1));
    }
    
    @Deprecated
    public void ensureIndex(final DBObject keys) {
        this.ensureIndex(keys, this.defaultOptions(keys));
    }
    
    @Deprecated
    public void ensureIndex(final DBObject keys, final String name) {
        this.ensureIndex(keys, name, false);
    }
    
    @Deprecated
    public void ensureIndex(final DBObject keys, final String name, final boolean unique) {
        final DBObject options = this.defaultOptions(keys);
        if (name != null && name.length() > 0) {
            options.put("name", name);
        }
        if (unique) {
            options.put("unique", Boolean.TRUE);
        }
        this.ensureIndex(keys, options);
    }
    
    @Deprecated
    public void ensureIndex(final DBObject keys, final DBObject optionsIN) {
        if (this.checkReadOnly(false)) {
            return;
        }
        final DBObject options = this.defaultOptions(keys);
        for (final String k : optionsIN.keySet()) {
            options.put(k, optionsIN.get(k));
        }
        final String name = options.get("name").toString();
        if (this._createdIndexes.contains(name)) {
            return;
        }
        this.createIndex(keys, options);
        this._createdIndexes.add(name);
    }
    
    @Deprecated
    public void resetIndexCache() {
        this._createdIndexes.clear();
    }
    
    DBObject defaultOptions(final DBObject keys) {
        final DBObject o = new BasicDBObject();
        o.put("name", genIndexName(keys));
        o.put("ns", this._fullName);
        return o;
    }
    
    @Deprecated
    public static String genIndexName(final DBObject keys) {
        final StringBuilder name = new StringBuilder();
        for (final String s : keys.keySet()) {
            if (name.length() > 0) {
                name.append('_');
            }
            name.append(s).append('_');
            final Object val = keys.get(s);
            if (val instanceof Number || val instanceof String) {
                name.append(val.toString().replace(' ', '_'));
            }
        }
        return name.toString();
    }
    
    public void setHintFields(final List<DBObject> indexes) {
        this._hintFields = indexes;
    }
    
    protected List<DBObject> getHintFields() {
        return this._hintFields;
    }
    
    public DBCursor find(final DBObject ref) {
        return new DBCursor(this, ref, null, this.getReadPreference());
    }
    
    public DBCursor find(final DBObject ref, final DBObject keys) {
        return new DBCursor(this, ref, keys, this.getReadPreference());
    }
    
    public DBCursor find() {
        return new DBCursor(this, null, null, this.getReadPreference());
    }
    
    public DBObject findOne() {
        return this.findOne(new BasicDBObject());
    }
    
    public DBObject findOne(final DBObject o) {
        return this.findOne(o, null, null, this.getReadPreference());
    }
    
    public DBObject findOne(final DBObject o, final DBObject fields) {
        return this.findOne(o, fields, null, this.getReadPreference());
    }
    
    public DBObject findOne(final DBObject o, final DBObject fields, final DBObject orderBy) {
        return this.findOne(o, fields, orderBy, this.getReadPreference());
    }
    
    public DBObject findOne(final DBObject o, final DBObject fields, final ReadPreference readPref) {
        return this.findOne(o, fields, null, readPref);
    }
    
    public DBObject findOne(final DBObject o, final DBObject fields, final DBObject orderBy, final ReadPreference readPref) {
        return this.findOne(o, fields, orderBy, readPref, 0L, TimeUnit.MILLISECONDS);
    }
    
    DBObject findOne(final DBObject o, final DBObject fields, final DBObject orderBy, final ReadPreference readPref, final long maxTime, final TimeUnit maxTimeUnit) {
        final QueryOpBuilder queryOpBuilder = new QueryOpBuilder().addQuery(o).addOrderBy(orderBy).addMaxTimeMS(TimeUnit.MILLISECONDS.convert(maxTime, maxTimeUnit));
        if (this.getDB().getMongo().isMongosConnection()) {
            queryOpBuilder.addReadPreference(readPref);
        }
        final Iterator<DBObject> i = this.find(queryOpBuilder.get(), fields, 0, -1, 0, this.getOptions(), readPref, this.getDecoder());
        final DBObject obj = i.hasNext() ? i.next() : null;
        if (obj != null && fields != null && fields.keySet().size() > 0) {
            obj.markAsPartialObject();
        }
        return obj;
    }
    
    DBDecoder getDecoder() {
        return (this.getDBDecoderFactory() != null) ? this.getDBDecoderFactory().create() : null;
    }
    
    private DBEncoder getDBEncoder() {
        return (this.getDBEncoderFactory() != null) ? this.getDBEncoderFactory().create() : null;
    }
    
    @Deprecated
    public Object apply(final DBObject document) {
        return this.apply(document, true);
    }
    
    @Deprecated
    public Object apply(final DBObject document, final boolean ensureId) {
        Object id = document.get("_id");
        if (ensureId && id == null) {
            id = ObjectId.get();
            document.put("_id", id);
        }
        this.doapply(document);
        return id;
    }
    
    public WriteResult save(final DBObject jo) {
        return this.save(jo, this.getWriteConcern());
    }
    
    public WriteResult save(final DBObject jo, final WriteConcern concern) {
        if (this.checkReadOnly(true)) {
            return null;
        }
        this._checkObject(jo, false, false);
        final Object id = jo.get("_id");
        if (id == null || (id instanceof ObjectId && ((ObjectId)id).isNew())) {
            if (id != null) {
                ((ObjectId)id).notNew();
            }
            if (concern == null) {
                return this.insert(jo);
            }
            return this.insert(jo, concern);
        }
        else {
            final DBObject q = new BasicDBObject();
            q.put("_id", id);
            if (concern == null) {
                return this.update(q, jo, true, false);
            }
            return this.update(q, jo, true, false, concern);
        }
    }
    
    public void dropIndexes() {
        this.dropIndexes("*");
    }
    
    public void dropIndexes(final String name) {
        final DBObject cmd = BasicDBObjectBuilder.start().add("deleteIndexes", this.getName()).add("index", name).get();
        this.resetIndexCache();
        final CommandResult res = this._db.command(cmd);
        if (res.ok() || res.getErrorMessage().contains("ns not found")) {
            return;
        }
        res.throwOnError();
    }
    
    public void drop() {
        this.resetIndexCache();
        final CommandResult res = this._db.command(BasicDBObjectBuilder.start().add("drop", this.getName()).get());
        if (res.ok() || res.getErrorMessage().contains("ns not found")) {
            return;
        }
        res.throwOnError();
    }
    
    public long count() {
        return this.getCount(new BasicDBObject(), null);
    }
    
    public long count(final DBObject query) {
        return this.getCount(query, null);
    }
    
    public long count(final DBObject query, final ReadPreference readPrefs) {
        return this.getCount(query, null, readPrefs);
    }
    
    public long getCount() {
        return this.getCount(new BasicDBObject(), null);
    }
    
    public long getCount(final ReadPreference readPrefs) {
        return this.getCount(new BasicDBObject(), null, readPrefs);
    }
    
    public long getCount(final DBObject query) {
        return this.getCount(query, null);
    }
    
    public long getCount(final DBObject query, final DBObject fields) {
        return this.getCount(query, fields, 0L, 0L);
    }
    
    public long getCount(final DBObject query, final DBObject fields, final ReadPreference readPrefs) {
        return this.getCount(query, fields, 0L, 0L, readPrefs);
    }
    
    public long getCount(final DBObject query, final DBObject fields, final long limit, final long skip) {
        return this.getCount(query, fields, limit, skip, this.getReadPreference());
    }
    
    public long getCount(final DBObject query, final DBObject fields, final long limit, final long skip, final ReadPreference readPrefs) {
        return this.getCount(query, fields, limit, skip, readPrefs, 0L, TimeUnit.MILLISECONDS);
    }
    
    long getCount(final DBObject query, final DBObject fields, final long limit, final long skip, final ReadPreference readPrefs, final long maxTime, final TimeUnit maxTimeUnit) {
        return this.getCount(query, fields, limit, skip, readPrefs, maxTime, maxTimeUnit, null);
    }
    
    long getCount(final DBObject query, final DBObject fields, final long limit, final long skip, final ReadPreference readPrefs, final long maxTime, final TimeUnit maxTimeUnit, final Object hint) {
        final BasicDBObject cmd = new BasicDBObject();
        cmd.put("count", this.getName());
        cmd.put("query", query);
        if (fields != null) {
            cmd.put("fields", fields);
        }
        if (limit > 0L) {
            cmd.put("limit", limit);
        }
        if (skip > 0L) {
            cmd.put("skip", skip);
        }
        if (maxTime > 0L) {
            cmd.put("maxTimeMS", TimeUnit.MILLISECONDS.convert(maxTime, maxTimeUnit));
        }
        if (hint != null) {
            cmd.put("hint", hint);
        }
        final CommandResult res = this._db.command(cmd, this.getOptions(), readPrefs);
        if (!res.ok()) {
            final String errmsg = res.getErrorMessage();
            if (errmsg.equals("ns does not exist") || errmsg.equals("ns missing")) {
                return 0L;
            }
            res.throwOnError();
        }
        return res.getLong("n");
    }
    
    CommandResult command(final DBObject cmd, final int options, final ReadPreference readPrefs) {
        return this._db.command(cmd, this.getOptions(), readPrefs);
    }
    
    public DBCollection rename(final String newName) {
        return this.rename(newName, false);
    }
    
    public DBCollection rename(final String newName, final boolean dropTarget) {
        final CommandResult ret = this._db.getSisterDB("admin").command(BasicDBObjectBuilder.start().add("renameCollection", this._fullName).add("to", this._db._name + "." + newName).add("dropTarget", dropTarget).get());
        ret.throwOnError();
        this.resetIndexCache();
        return this._db.getCollection(newName);
    }
    
    public DBObject group(final DBObject key, final DBObject cond, final DBObject initial, final String reduce) {
        return this.group(key, cond, initial, reduce, null);
    }
    
    public DBObject group(final DBObject key, final DBObject cond, final DBObject initial, final String reduce, final String finalize) {
        final GroupCommand cmd = new GroupCommand(this, key, cond, initial, reduce, finalize);
        return this.group(cmd);
    }
    
    public DBObject group(final DBObject key, final DBObject cond, final DBObject initial, final String reduce, final String finalize, final ReadPreference readPrefs) {
        final GroupCommand cmd = new GroupCommand(this, key, cond, initial, reduce, finalize);
        return this.group(cmd, readPrefs);
    }
    
    public DBObject group(final GroupCommand cmd) {
        return this.group(cmd, this.getReadPreference());
    }
    
    public DBObject group(final GroupCommand cmd, final ReadPreference readPrefs) {
        final CommandResult res = this._db.command(cmd.toDBObject(), this.getOptions(), readPrefs);
        res.throwOnError();
        return (DBObject)res.get("retval");
    }
    
    @Deprecated
    public DBObject group(final DBObject args) {
        args.put("ns", this.getName());
        final CommandResult res = this._db.command(new BasicDBObject("group", args), this.getOptions(), this.getReadPreference());
        res.throwOnError();
        return (DBObject)res.get("retval");
    }
    
    public List distinct(final String key) {
        return this.distinct(key, new BasicDBObject());
    }
    
    public List distinct(final String key, final ReadPreference readPrefs) {
        return this.distinct(key, new BasicDBObject(), readPrefs);
    }
    
    public List distinct(final String key, final DBObject query) {
        return this.distinct(key, query, this.getReadPreference());
    }
    
    public List distinct(final String key, final DBObject query, final ReadPreference readPrefs) {
        final DBObject c = BasicDBObjectBuilder.start().add("distinct", this.getName()).add("key", key).add("query", query).get();
        final CommandResult res = this._db.command(c, this.getOptions(), readPrefs);
        res.throwOnError();
        return (List)res.get("values");
    }
    
    public MapReduceOutput mapReduce(final String map, final String reduce, final String outputTarget, final DBObject query) {
        return this.mapReduce(new MapReduceCommand(this, map, reduce, outputTarget, MapReduceCommand.OutputType.REPLACE, query));
    }
    
    public MapReduceOutput mapReduce(final String map, final String reduce, final String outputTarget, final MapReduceCommand.OutputType outputType, final DBObject query) {
        return this.mapReduce(new MapReduceCommand(this, map, reduce, outputTarget, outputType, query));
    }
    
    public MapReduceOutput mapReduce(final String map, final String reduce, final String outputTarget, final MapReduceCommand.OutputType outputType, final DBObject query, final ReadPreference readPrefs) {
        final MapReduceCommand command = new MapReduceCommand(this, map, reduce, outputTarget, outputType, query);
        command.setReadPreference(readPrefs);
        return this.mapReduce(command);
    }
    
    public MapReduceOutput mapReduce(final MapReduceCommand command) {
        final DBObject cmd = command.toDBObject();
        final CommandResult res = this._db.command(cmd, this.getOptions(), (command.getReadPreference() != null) ? command.getReadPreference() : this.getReadPreference());
        res.throwOnError();
        return new MapReduceOutput(this, cmd, res);
    }
    
    @Deprecated
    public MapReduceOutput mapReduce(final DBObject command) {
        if (command.get("mapreduce") == null && command.get("mapReduce") == null) {
            throw new IllegalArgumentException("need mapreduce arg");
        }
        final CommandResult res = this._db.command(command, this.getOptions(), this.getReadPreference());
        res.throwOnError();
        return new MapReduceOutput(this, command, res);
    }
    
    @Deprecated
    public AggregationOutput aggregate(final DBObject firstOp, final DBObject... additionalOps) {
        final List<DBObject> pipeline = new ArrayList<DBObject>();
        pipeline.add(firstOp);
        Collections.addAll(pipeline, additionalOps);
        return this.aggregate(pipeline);
    }
    
    public AggregationOutput aggregate(final List<DBObject> pipeline) {
        return this.aggregate(pipeline, this.getReadPreference());
    }
    
    public AggregationOutput aggregate(final List<DBObject> pipeline, final ReadPreference readPreference) {
        final AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.INLINE).build();
        final DBObject command = this.prepareCommand(pipeline, options);
        final CommandResult res = this._db.command(command, this.getOptions(), readPreference);
        res.throwOnError();
        return new AggregationOutput(command, res);
    }
    
    public Cursor aggregate(final List<DBObject> pipeline, final AggregationOptions options) {
        return this.aggregate(pipeline, options, this.getReadPreference());
    }
    
    public abstract Cursor aggregate(final List<DBObject> p0, final AggregationOptions p1, final ReadPreference p2);
    
    public CommandResult explainAggregate(final List<DBObject> pipeline, final AggregationOptions options) {
        final DBObject command = this.prepareCommand(pipeline, options);
        command.put("explain", true);
        final CommandResult res = this._db.command(command, this.getOptions(), this.getReadPreference());
        res.throwOnError();
        return res;
    }
    
    public abstract List<Cursor> parallelScan(final ParallelScanOptions p0);
    
    public BulkWriteOperation initializeOrderedBulkOperation() {
        return new BulkWriteOperation(true, this);
    }
    
    public BulkWriteOperation initializeUnorderedBulkOperation() {
        return new BulkWriteOperation(false, this);
    }
    
    BulkWriteResult executeBulkWriteOperation(final boolean ordered, final List<WriteRequest> requests) {
        return this.executeBulkWriteOperation(ordered, requests, this.getWriteConcern());
    }
    
    BulkWriteResult executeBulkWriteOperation(final boolean ordered, final List<WriteRequest> requests, final WriteConcern writeConcern) {
        return this.executeBulkWriteOperation(ordered, requests, writeConcern, this.getDBEncoder());
    }
    
    abstract BulkWriteResult executeBulkWriteOperation(final boolean p0, final List<WriteRequest> p1, final WriteConcern p2, final DBEncoder p3);
    
    DBObject prepareCommand(final List<DBObject> pipeline, final AggregationOptions options) {
        if (pipeline.isEmpty()) {
            throw new MongoException("Aggregation pipelines can not be empty");
        }
        final DBObject command = new BasicDBObject("aggregate", this.getName());
        command.put("pipeline", pipeline);
        if (options.getOutputMode() == AggregationOptions.OutputMode.CURSOR) {
            final BasicDBObject cursor = new BasicDBObject();
            if (options.getBatchSize() != null) {
                cursor.put("batchSize", options.getBatchSize());
            }
            command.put("cursor", cursor);
        }
        if (options.getMaxTime(TimeUnit.MILLISECONDS) > 0L) {
            command.put("maxTimeMS", options.getMaxTime(TimeUnit.MILLISECONDS));
        }
        if (options.getAllowDiskUse() != null) {
            command.put("allowDiskUse", options.getAllowDiskUse());
        }
        return command;
    }
    
    public abstract List<DBObject> getIndexInfo();
    
    public void dropIndex(final DBObject keys) {
        this.dropIndexes(genIndexName(keys));
    }
    
    public void dropIndex(final String indexName) {
        this.dropIndexes(indexName);
    }
    
    public CommandResult getStats() {
        return this.getDB().command(new BasicDBObject("collstats", this.getName()), this.getOptions(), this.getReadPreference());
    }
    
    public boolean isCapped() {
        final CommandResult stats = this.getStats();
        final Object capped = stats.get("capped");
        return capped != null && (capped.equals(1) || capped.equals(true));
    }
    
    protected DBCollection(final DB base, final String name) {
        super();
        this._concern = null;
        this._readPref = null;
        this._objectClass = null;
        this._internalClass = (Map<String, Class>)Collections.synchronizedMap(new HashMap<String, Class>());
        this._wrapper = null;
        this._createdIndexes = new HashSet<String>();
        this._db = base;
        this._name = name;
        this._fullName = this._db.getName() + "." + name;
        this._options = new Bytes.OptionHolder(this._db._options);
    }
    
    @Deprecated
    protected DBObject _checkObject(final DBObject o, final boolean canBeNull, final boolean query) {
        if (o == null) {
            if (canBeNull) {
                return null;
            }
            throw new IllegalArgumentException("can't be null");
        }
        else {
            if (o.isPartialObject() && !query) {
                throw new IllegalArgumentException("can't save partial objects");
            }
            if (!query) {
                this._checkKeys(o);
            }
            return o;
        }
    }
    
    private void _checkKeys(final DBObject o) {
        if (o instanceof LazyDBObject || o instanceof LazyDBList) {
            return;
        }
        for (final String s : o.keySet()) {
            this.validateKey(s);
            this._checkValue(o.get(s));
        }
    }
    
    private void _checkKeys(final Map<String, Object> o) {
        for (final Map.Entry<String, Object> cur : o.entrySet()) {
            this.validateKey(cur.getKey());
            this._checkValue(cur.getValue());
        }
    }
    
    private void _checkValues(final List list) {
        for (final Object cur : list) {
            this._checkValue(cur);
        }
    }
    
    private void _checkValue(final Object value) {
        if (value instanceof DBObject) {
            this._checkKeys((DBObject)value);
        }
        else if (value instanceof Map) {
            this._checkKeys((Map<String, Object>)value);
        }
        else if (value instanceof List) {
            this._checkValues((List)value);
        }
    }
    
    private void validateKey(final String s) {
        if (s.contains("\u0000")) {
            throw new IllegalArgumentException("Document field names can't have a NULL character. (Bad Key: '" + s + "')");
        }
        if (s.contains(".")) {
            throw new IllegalArgumentException("Document field names can't have a . in them. (Bad Key: '" + s + "')");
        }
        if (s.startsWith("$")) {
            throw new IllegalArgumentException("Document field names can't start with '$' (Bad Key: '" + s + "')");
        }
    }
    
    public DBCollection getCollection(final String n) {
        return this._db.getCollection(this._name + "." + n);
    }
    
    public String getName() {
        return this._name;
    }
    
    public String getFullName() {
        return this._fullName;
    }
    
    public DB getDB() {
        return this._db;
    }
    
    @Deprecated
    protected boolean checkReadOnly(final boolean strict) {
        if (!this._db._readOnly) {
            return false;
        }
        if (!strict) {
            return true;
        }
        throw new IllegalStateException("db is read only");
    }
    
    public int hashCode() {
        return this._fullName.hashCode();
    }
    
    public boolean equals(final Object o) {
        return o == this;
    }
    
    public String toString() {
        return this._name;
    }
    
    public void setObjectClass(final Class c) {
        if (c == null) {
            this._wrapper = null;
            this._objectClass = null;
            return;
        }
        if (!DBObject.class.isAssignableFrom(c)) {
            throw new IllegalArgumentException(c.getName() + " is not a DBObject");
        }
        this._objectClass = c;
        if (ReflectionDBObject.class.isAssignableFrom(c)) {
            this._wrapper = ReflectionDBObject.getWrapper(c);
        }
        else {
            this._wrapper = null;
        }
    }
    
    public Class getObjectClass() {
        return this._objectClass;
    }
    
    public void setInternalClass(final String path, final Class c) {
        this._internalClass.put(path, c);
    }
    
    protected Class getInternalClass(final String path) {
        final Class c = this._internalClass.get(path);
        if (c != null) {
            return c;
        }
        if (this._wrapper == null) {
            return null;
        }
        return this._wrapper.getInternalClass(path);
    }
    
    public void setWriteConcern(final WriteConcern writeConcern) {
        this._concern = writeConcern;
    }
    
    public WriteConcern getWriteConcern() {
        if (this._concern != null) {
            return this._concern;
        }
        return this._db.getWriteConcern();
    }
    
    public void setReadPreference(final ReadPreference preference) {
        this._readPref = preference;
    }
    
    public ReadPreference getReadPreference() {
        if (this._readPref != null) {
            return this._readPref;
        }
        return this._db.getReadPreference();
    }
    
    @Deprecated
    public void slaveOk() {
        this.addOption(4);
    }
    
    public void addOption(final int option) {
        this._options.add(option);
    }
    
    public void setOptions(final int options) {
        this._options.set(options);
    }
    
    public void resetOptions() {
        this._options.reset();
    }
    
    public int getOptions() {
        return this._options.get();
    }
    
    public synchronized void setDBDecoderFactory(final DBDecoderFactory fact) {
        this._decoderFactory = fact;
    }
    
    public synchronized DBDecoderFactory getDBDecoderFactory() {
        return this._decoderFactory;
    }
    
    public synchronized void setDBEncoderFactory(final DBEncoderFactory fact) {
        this._encoderFactory = fact;
    }
    
    public synchronized DBEncoderFactory getDBEncoderFactory() {
        return this._encoderFactory;
    }
}
