package com.mongodb;

import java.util.logging.*;
import com.mongodb.util.*;
import org.bson.util.*;
import org.bson.*;
import java.io.*;
import org.bson.types.*;
import org.bson.io.*;
import java.util.*;

class DBCollectionImpl extends DBCollection
{
    private static final int QUERY_DOCUMENT_HEADROOM = 16384;
    private final DBApiLayer db;
    private final String namespace;
    private static final Logger TRACE_LOGGER;
    private static final Level TRACE_LEVEL;
    
    DBCollectionImpl(final DBApiLayer db, final String name) {
        super(db, name);
        this.namespace = db._root + "." + name;
        this.db = db;
    }
    
    QueryResultIterator find(final DBObject ref, final DBObject fields, final int numToSkip, final int batchSize, final int limit, final int options, final ReadPreference readPref, final DBDecoder decoder) {
        return this.find(ref, fields, numToSkip, batchSize, limit, options, readPref, decoder, DefaultDBEncoder.FACTORY.create());
    }
    
    QueryResultIterator find(DBObject ref, final DBObject fields, final int numToSkip, final int batchSize, final int limit, final int options, final ReadPreference readPref, final DBDecoder decoder, final DBEncoder encoder) {
        if (ref == null) {
            ref = new BasicDBObject();
        }
        if (this.willTrace()) {
            this.trace("find: " + this.namespace + " " + JSON.serialize(ref));
        }
        final OutMessage query = OutMessage.query(this, options, numToSkip, QueryResultIterator.chooseBatchSize(batchSize, limit, 0), ref, fields, readPref, encoder, this.db.getMongo().getMaxBsonObjectSize() + 16384);
        final Response res = this.db.getConnector().call(this._db, this, query, null, 2, readPref, decoder);
        return new QueryResultIterator(this.db, this, res, batchSize, limit, options, decoder);
    }
    
    public Cursor aggregate(final List<DBObject> pipeline, final AggregationOptions options, final ReadPreference readPreference) {
        if (options == null) {
            throw new IllegalArgumentException("options can not be null");
        }
        final DBObject last = pipeline.get(pipeline.size() - 1);
        final DBObject command = this.prepareCommand(pipeline, options);
        final CommandResult res = this._db.command(command, this.getOptions(), readPreference);
        res.throwOnError();
        final String outCollection = (String)last.get("$out");
        if (outCollection != null) {
            final DBCollection collection = this._db.getCollection(outCollection);
            return new DBCursor(collection, new BasicDBObject(), null, ReadPreference.primary());
        }
        final Integer batchSize = options.getBatchSize();
        return new QueryResultIterator(res, this.db, this, (batchSize == null) ? 0 : batchSize, this.getDecoder(), res.getServerUsed());
    }
    
    public List<Cursor> parallelScan(final ParallelScanOptions options) {
        final CommandResult res = this._db.command(new BasicDBObject("parallelCollectionScan", this.getName()).append("numCursors", options.getNumCursors()), options.getReadPreference());
        res.throwOnError();
        final List<Cursor> cursors = new ArrayList<Cursor>();
        for (final DBObject cursorDocument : (List)res.get("cursors")) {
            cursors.add(new QueryResultIterator(cursorDocument, this.db, this, options.getBatchSize(), this.getDecoder(), res.getServerUsed()));
        }
        return cursors;
    }
    
    BulkWriteResult executeBulkWriteOperation(final boolean ordered, final List<WriteRequest> writeRequests, WriteConcern writeConcern, DBEncoder encoder) {
        Assertions.isTrue("no operations", !writeRequests.isEmpty());
        if (writeConcern == null) {
            throw new IllegalArgumentException("Write concern can not be null");
        }
        writeConcern = writeConcern.continueOnError(!ordered);
        if (encoder == null) {
            encoder = DefaultDBEncoder.FACTORY.create();
        }
        final DBPort port = this.db.getConnector().getPrimaryPort();
        try {
            final BulkWriteBatchCombiner bulkWriteBatchCombiner = new BulkWriteBatchCombiner(port.getAddress(), writeConcern);
            for (final Run run : this.getRunGenerator(ordered, writeRequests, writeConcern, encoder, port)) {
                try {
                    final BulkWriteResult result = run.execute(port);
                    if (!result.isAcknowledged()) {
                        continue;
                    }
                    bulkWriteBatchCombiner.addResult(result, run.indexMap);
                }
                catch (BulkWriteException e) {
                    bulkWriteBatchCombiner.addErrorResult(e, run.indexMap);
                    if (bulkWriteBatchCombiner.shouldStopSendingMoreBatches()) {
                        break;
                    }
                    continue;
                }
            }
            return bulkWriteBatchCombiner.getResult();
        }
        finally {
            this.db.getConnector().releasePort(port);
        }
    }
    
    public WriteResult insert(final List<DBObject> list, final WriteConcern concern, final DBEncoder encoder) {
        return this.insert(list, true, concern, encoder);
    }
    
    protected WriteResult insert(final List<DBObject> list, final boolean shouldApply, final WriteConcern concern, DBEncoder encoder) {
        if (concern == null) {
            throw new IllegalArgumentException("Write concern can not be null");
        }
        if (encoder == null) {
            encoder = DefaultDBEncoder.FACTORY.create();
        }
        if (this.willTrace()) {
            for (final DBObject o : list) {
                this.trace("save:  " + this.namespace + " " + JSON.serialize(o));
            }
        }
        final DBPort port = this.db.getConnector().getPrimaryPort();
        try {
            if (this.useWriteCommands(concern, port)) {
                try {
                    return this.translateBulkWriteResult(this.insertWithCommandProtocol(list, concern, encoder, port, shouldApply), WriteRequest.Type.INSERT, concern, port.getAddress());
                }
                catch (BulkWriteException e) {
                    throw this.translateBulkWriteException(e, WriteRequest.Type.INSERT);
                }
            }
            return this.insertWithWriteProtocol(list, concern, encoder, port, shouldApply);
        }
        finally {
            this.db.getConnector().releasePort(port);
        }
    }
    
    public WriteResult remove(final DBObject query, final WriteConcern concern, final DBEncoder encoder) {
        return this.remove(query, true, concern, encoder);
    }
    
    public WriteResult remove(final DBObject query, final boolean multi, final WriteConcern concern, DBEncoder encoder) {
        if (concern == null) {
            throw new IllegalArgumentException("Write concern can not be null");
        }
        if (encoder == null) {
            encoder = DefaultDBEncoder.FACTORY.create();
        }
        if (this.willTrace()) {
            this.trace("remove: " + this.namespace + " " + JSON.serialize(query));
        }
        final DBPort port = this.db.getConnector().getPrimaryPort();
        try {
            if (this.useWriteCommands(concern, port)) {
                try {
                    return this.translateBulkWriteResult(this.removeWithCommandProtocol(Arrays.asList(new RemoveRequest(query, multi)), concern, encoder, port), WriteRequest.Type.REMOVE, concern, port.getAddress());
                }
                catch (BulkWriteException e) {
                    throw this.translateBulkWriteException(e, WriteRequest.Type.REMOVE);
                }
            }
            return this.db.getConnector().say(this._db, OutMessage.remove(this, encoder, query, multi), concern, port);
        }
        finally {
            this.db.getConnector().releasePort(port);
        }
    }
    
    public WriteResult update(final DBObject query, final DBObject o, final boolean upsert, final boolean multi, final WriteConcern concern, DBEncoder encoder) {
        if (o == null) {
            throw new IllegalArgumentException("update can not be null");
        }
        if (concern == null) {
            throw new IllegalArgumentException("Write concern can not be null");
        }
        if (encoder == null) {
            encoder = DefaultDBEncoder.FACTORY.create();
        }
        if (!o.keySet().isEmpty()) {
            final String key = o.keySet().iterator().next();
            if (!key.startsWith("$")) {
                this._checkObject(o, false, false);
            }
        }
        if (this.willTrace()) {
            this.trace("update: " + this.namespace + " " + JSON.serialize(query) + " " + JSON.serialize(o));
        }
        final DBPort port = this.db.getConnector().getPrimaryPort();
        try {
            if (this.useWriteCommands(concern, port)) {
                try {
                    final BulkWriteResult bulkWriteResult = this.updateWithCommandProtocol(Arrays.asList(new UpdateRequest(query, upsert, o, multi)), concern, encoder, port);
                    return this.translateBulkWriteResult(bulkWriteResult, WriteRequest.Type.UPDATE, concern, port.getAddress());
                }
                catch (BulkWriteException e) {
                    throw this.translateBulkWriteException(e, WriteRequest.Type.UPDATE);
                }
            }
            return this.db.getConnector().say(this._db, OutMessage.update(this, encoder, upsert, multi, query, o), concern, port);
        }
        finally {
            this.db.getConnector().releasePort(port);
        }
    }
    
    public void drop() {
        this.db._collections.remove(this.getName());
        super.drop();
    }
    
    public void doapply(final DBObject o) {
    }
    
    private WriteResult translateBulkWriteResult(final BulkWriteResult bulkWriteResult, final WriteRequest.Type type, final WriteConcern writeConcern, final ServerAddress serverAddress) {
        final CommandResult commandResult = new CommandResult(serverAddress);
        this.addBulkWriteResultToCommandResult(bulkWriteResult, type, commandResult);
        return new WriteResult(commandResult, writeConcern);
    }
    
    private MongoException translateBulkWriteException(final BulkWriteException e, final WriteRequest.Type type) {
        final BulkWriteError lastError = e.getWriteErrors().isEmpty() ? null : e.getWriteErrors().get(e.getWriteErrors().size() - 1);
        final CommandResult commandResult = new CommandResult(e.getServerAddress());
        this.addBulkWriteResultToCommandResult(e.getWriteResult(), type, commandResult);
        if (e.getWriteConcernError() != null) {
            commandResult.putAll(e.getWriteConcernError().getDetails());
        }
        if (lastError != null) {
            commandResult.put("err", lastError.getMessage());
            commandResult.put("code", lastError.getCode());
            commandResult.putAll(lastError.getDetails());
        }
        else if (e.getWriteConcernError() != null) {
            commandResult.put("err", e.getWriteConcernError().getMessage());
            commandResult.put("code", e.getWriteConcernError().getCode());
        }
        return commandResult.getException();
    }
    
    private void addBulkWriteResultToCommandResult(final BulkWriteResult bulkWriteResult, final WriteRequest.Type type, final CommandResult commandResult) {
        commandResult.put("ok", 1);
        if (type == WriteRequest.Type.INSERT) {
            commandResult.put("n", 0);
        }
        else if (type == WriteRequest.Type.REMOVE) {
            commandResult.put("n", bulkWriteResult.getRemovedCount());
        }
        else if (type == WriteRequest.Type.UPDATE || type == WriteRequest.Type.REPLACE) {
            commandResult.put("n", bulkWriteResult.getMatchedCount() + bulkWriteResult.getUpserts().size());
            if (bulkWriteResult.getMatchedCount() > 0) {
                commandResult.put("updatedExisting", true);
            }
            else {
                commandResult.put("updatedExisting", false);
            }
            if (!bulkWriteResult.getUpserts().isEmpty()) {
                commandResult.put("upserted", bulkWriteResult.getUpserts().get(0).getId());
            }
        }
    }
    
    public List<DBObject> getIndexInfo() {
        this.getDB().requestStart();
        try {
            final List<DBObject> list = new ArrayList<DBObject>();
            if (this.db.isServerVersionAtLeast(Arrays.asList(2, 7, 6))) {
                final CommandResult res = this._db.command(new BasicDBObject("listIndexes", this.getName()), ReadPreference.primary());
                if (!res.ok() && res.getCode() == 26) {
                    return list;
                }
                res.throwOnError();
                for (final DBObject indexDocument : (List)res.get("indexes")) {
                    list.add(indexDocument);
                }
            }
            else {
                final BasicDBObject cmd = new BasicDBObject("ns", this.getFullName());
                final DBCursor cur = this._db.getCollection("system.indexes").find(cmd).setReadPreference(ReadPreference.primary());
                while (cur.hasNext()) {
                    list.add(cur.next());
                }
            }
            return list;
        }
        finally {
            this.getDB().requestDone();
        }
    }
    
    public void createIndex(final DBObject keys, final DBObject options, final DBEncoder encoder) {
        final DBTCPConnector connector = this.db.getConnector();
        final DBPort port = this.db.getConnector().getPrimaryPort();
        try {
            final DBObject index = this.defaultOptions(keys);
            index.putAll(options);
            index.put("key", keys);
            if (connector.getServerDescription(port.getAddress()).getVersion().compareTo(new ServerVersion(2, 6)) >= 0) {
                final BasicDBObject createIndexes = new BasicDBObject("createIndexes", this.getName());
                final BasicDBList list = new BasicDBList();
                ((ArrayList<DBObject>)list).add(index);
                createIndexes.put("indexes", list);
                final CommandResult commandResult = connector.doOperation(this.db, port, (DBPort.Operation<CommandResult>)new DBPort.Operation<CommandResult>() {
                    public CommandResult execute() throws IOException {
                        return port.runCommand(DBCollectionImpl.this.db, createIndexes);
                    }
                });
                try {
                    commandResult.throwOnError();
                }
                catch (CommandFailureException e) {
                    if (e.getCode() == 11000) {
                        throw new MongoException.DuplicateKey(commandResult);
                    }
                    throw e;
                }
            }
            else {
                this.db.doGetCollection("system.indexes").insertWithWriteProtocol(Arrays.asList(index), WriteConcern.SAFE, DefaultDBEncoder.FACTORY.create(), port, false);
            }
        }
        finally {
            connector.releasePort(port);
        }
    }
    
    private BulkWriteResult insertWithCommandProtocol(final List<DBObject> list, final WriteConcern writeConcern, final DBEncoder encoder, final DBPort port, final boolean shouldApply) {
        if (shouldApply) {
            this.applyRulesForInsert(list);
        }
        final BaseWriteCommandMessage message = new InsertCommandMessage(this.getNamespace(), writeConcern, list, DefaultDBEncoder.FACTORY.create(), encoder, this.getMessageSettings(port));
        return this.writeWithCommandProtocol(port, WriteRequest.Type.INSERT, message, writeConcern);
    }
    
    private void applyRulesForInsert(final List<DBObject> list) {
        for (final DBObject o : list) {
            this._checkObject(o, false, false);
            this.apply(o);
            final Object id = o.get("_id");
            if (id instanceof ObjectId) {
                ((ObjectId)id).notNew();
            }
        }
    }
    
    private BulkWriteResult removeWithCommandProtocol(final List<RemoveRequest> removeList, final WriteConcern writeConcern, final DBEncoder encoder, final DBPort port) {
        final BaseWriteCommandMessage message = new DeleteCommandMessage(this.getNamespace(), writeConcern, removeList, DefaultDBEncoder.FACTORY.create(), encoder, this.getMessageSettings(port));
        return this.writeWithCommandProtocol(port, WriteRequest.Type.REMOVE, message, writeConcern);
    }
    
    private BulkWriteResult updateWithCommandProtocol(final List<ModifyRequest> updates, final WriteConcern writeConcern, final DBEncoder encoder, final DBPort port) {
        final BaseWriteCommandMessage message = new UpdateCommandMessage(this.getNamespace(), writeConcern, updates, DefaultDBEncoder.FACTORY.create(), encoder, this.getMessageSettings(port));
        return this.writeWithCommandProtocol(port, WriteRequest.Type.UPDATE, message, writeConcern);
    }
    
    private BulkWriteResult writeWithCommandProtocol(final DBPort port, final WriteRequest.Type type, BaseWriteCommandMessage message, final WriteConcern writeConcern) {
        int batchNum = 0;
        int currentRangeStartIndex = 0;
        final BulkWriteBatchCombiner bulkWriteBatchCombiner = new BulkWriteBatchCombiner(port.getAddress(), writeConcern);
        do {
            ++batchNum;
            final BaseWriteCommandMessage nextMessage = this.sendWriteCommandMessage(message, batchNum, port);
            final int itemCount = (nextMessage != null) ? (message.getItemCount() - nextMessage.getItemCount()) : message.getItemCount();
            final IndexMap indexMap = IndexMap.create(currentRangeStartIndex, itemCount);
            final CommandResult commandResult = this.receiveWriteCommandMessage(port);
            if ((this.willTrace() && nextMessage != null) || batchNum > 1) {
                this.getLogger().fine(String.format("Received response for batch %d", batchNum));
            }
            if (WriteCommandResultHelper.hasError(commandResult)) {
                bulkWriteBatchCombiner.addErrorResult(WriteCommandResultHelper.getBulkWriteException(type, commandResult), indexMap);
            }
            else {
                bulkWriteBatchCombiner.addResult(WriteCommandResultHelper.getBulkWriteResult(type, commandResult), indexMap);
            }
            currentRangeStartIndex += itemCount;
            message = nextMessage;
        } while (message != null && !bulkWriteBatchCombiner.shouldStopSendingMoreBatches());
        return bulkWriteBatchCombiner.getResult();
    }
    
    private boolean useWriteCommands(final WriteConcern concern, final DBPort port) {
        return concern.callGetLastError() && this.db.getConnector().getServerDescription(port.getAddress()).getVersion().compareTo(new ServerVersion(2, 6)) >= 0;
    }
    
    private MessageSettings getMessageSettings(final DBPort port) {
        final ServerDescription serverDescription = this.db.getConnector().getServerDescription(port.getAddress());
        return MessageSettings.builder().maxDocumentSize(serverDescription.getMaxDocumentSize()).maxMessageSize(serverDescription.getMaxMessageSize()).maxWriteBatchSize(serverDescription.getMaxWriteBatchSize()).build();
    }
    
    private int getMaxWriteBatchSize(final DBPort port) {
        return this.db.getConnector().getServerDescription(port.getAddress()).getMaxWriteBatchSize();
    }
    
    private MongoNamespace getNamespace() {
        return new MongoNamespace(this.getDB().getName(), this.getName());
    }
    
    private BaseWriteCommandMessage sendWriteCommandMessage(final BaseWriteCommandMessage message, final int batchNum, final DBPort port) {
        final PoolOutputBuffer buffer = new PoolOutputBuffer();
        try {
            final BaseWriteCommandMessage nextMessage = message.encode(buffer);
            if (nextMessage != null || batchNum > 1) {
                this.getLogger().fine(String.format("Sending batch %d", batchNum));
            }
            this.db.getConnector().doOperation(this.getDB(), port, (DBPort.Operation<Object>)new DBPort.Operation<Void>() {
                public Void execute() throws IOException {
                    buffer.pipe(port.getOutputStream());
                    return null;
                }
            });
            return nextMessage;
        }
        finally {
            buffer.reset();
        }
    }
    
    private CommandResult receiveWriteCommandMessage(final DBPort port) {
        return this.db.getConnector().doOperation(this.getDB(), port, (DBPort.Operation<CommandResult>)new DBPort.Operation<CommandResult>() {
            public CommandResult execute() throws IOException {
                final Response response = new Response(port.getAddress(), null, port.getInputStream(), DefaultDBDecoder.FACTORY.create());
                final CommandResult writeCommandResult = new CommandResult(port.getAddress());
                writeCommandResult.putAll(response.get(0));
                writeCommandResult.throwOnError();
                return writeCommandResult;
            }
        });
    }
    
    private WriteResult insertWithWriteProtocol(final List<DBObject> list, final WriteConcern concern, final DBEncoder encoder, final DBPort port, final boolean shouldApply) {
        if (shouldApply) {
            this.applyRulesForInsert(list);
        }
        WriteResult last = null;
        int cur = 0;
        final int maxsize = this.db._mongo.getMaxBsonObjectSize();
        while (cur < list.size()) {
            final OutMessage om = OutMessage.insert(this, encoder, concern);
            while (cur < list.size()) {
                final DBObject o = list.get(cur);
                om.putObject(o);
                if (om.size() > 2 * maxsize) {
                    ++cur;
                    break;
                }
                ++cur;
            }
            last = this.db.getConnector().say(this._db, om, concern, port);
        }
        return last;
    }
    
    private Iterable<Run> getRunGenerator(final boolean ordered, final List<WriteRequest> writeRequests, final WriteConcern writeConcern, final DBEncoder encoder, final DBPort port) {
        if (ordered) {
            return new OrderedRunGenerator(writeRequests, writeConcern, encoder, port);
        }
        return new UnorderedRunGenerator(writeRequests, writeConcern, encoder, port);
    }
    
    private boolean willTrace() {
        return DBCollectionImpl.TRACE_LOGGER.isLoggable(DBCollectionImpl.TRACE_LEVEL);
    }
    
    private void trace(final String s) {
        DBCollectionImpl.TRACE_LOGGER.log(DBCollectionImpl.TRACE_LEVEL, s);
    }
    
    private Logger getLogger() {
        return DBCollectionImpl.TRACE_LOGGER;
    }
    
    static {
        TRACE_LOGGER = Logger.getLogger("com.mongodb.TRACE");
        TRACE_LEVEL = (Boolean.getBoolean("DB.TRACE") ? Level.INFO : Level.FINEST);
    }
    
    private class OrderedRunGenerator implements Iterable<Run>
    {
        private final List<WriteRequest> writeRequests;
        private final WriteConcern writeConcern;
        private final DBEncoder encoder;
        private final int maxBatchWriteSize;
        
        public OrderedRunGenerator(final List<WriteRequest> writeRequests, final WriteConcern writeConcern, final DBEncoder encoder, final DBPort port) {
            super();
            this.writeRequests = writeRequests;
            this.writeConcern = writeConcern.continueOnError(false);
            this.encoder = encoder;
            this.maxBatchWriteSize = DBCollectionImpl.this.getMaxWriteBatchSize(port);
        }
        
        public Iterator<Run> iterator() {
            return new Iterator<Run>() {
                private int curIndex;
                
                public boolean hasNext() {
                    return this.curIndex < OrderedRunGenerator.this.writeRequests.size();
                }
                
                public Run next() {
                    final Run run = new Run(OrderedRunGenerator.this.writeRequests.get(this.curIndex).getType(), OrderedRunGenerator.this.writeConcern, OrderedRunGenerator.this.encoder);
                    final int startIndexOfNextRun = this.getStartIndexOfNextRun();
                    for (int i = this.curIndex; i < startIndexOfNextRun; ++i) {
                        run.add(OrderedRunGenerator.this.writeRequests.get(i), i);
                    }
                    this.curIndex = startIndexOfNextRun;
                    return run;
                }
                
                private int getStartIndexOfNextRun() {
                    final WriteRequest.Type type = OrderedRunGenerator.this.writeRequests.get(this.curIndex).getType();
                    for (int i = this.curIndex; i < OrderedRunGenerator.this.writeRequests.size(); ++i) {
                        if (i == this.curIndex + OrderedRunGenerator.this.maxBatchWriteSize || OrderedRunGenerator.this.writeRequests.get(i).getType() != type) {
                            return i;
                        }
                    }
                    return OrderedRunGenerator.this.writeRequests.size();
                }
                
                public void remove() {
                    throw new UnsupportedOperationException("Not implemented");
                }
            };
        }
    }
    
    private class UnorderedRunGenerator implements Iterable<Run>
    {
        private final List<WriteRequest> writeRequests;
        private final WriteConcern writeConcern;
        private final DBEncoder encoder;
        private final int maxBatchWriteSize;
        
        public UnorderedRunGenerator(final List<WriteRequest> writeRequests, final WriteConcern writeConcern, final DBEncoder encoder, final DBPort port) {
            super();
            this.writeRequests = writeRequests;
            this.writeConcern = writeConcern.continueOnError(true);
            this.encoder = encoder;
            this.maxBatchWriteSize = DBCollectionImpl.this.getMaxWriteBatchSize(port);
        }
        
        public Iterator<Run> iterator() {
            return new Iterator<Run>() {
                private final Map<WriteRequest.Type, Run> runs = new TreeMap<WriteRequest.Type, Run>(new Comparator<WriteRequest.Type>() {
                    public int compare(final WriteRequest.Type first, final WriteRequest.Type second) {
                        return first.compareTo(second);
                    }
                });
                private int curIndex;
                
                public boolean hasNext() {
                    return this.curIndex < UnorderedRunGenerator.this.writeRequests.size() || !this.runs.isEmpty();
                }
                
                public Run next() {
                    while (this.curIndex < UnorderedRunGenerator.this.writeRequests.size()) {
                        final WriteRequest writeRequest = UnorderedRunGenerator.this.writeRequests.get(this.curIndex);
                        Run run = this.runs.get(writeRequest.getType());
                        if (run == null) {
                            run = new Run(writeRequest.getType(), UnorderedRunGenerator.this.writeConcern, UnorderedRunGenerator.this.encoder);
                            this.runs.put(run.type, run);
                        }
                        run.add(writeRequest, this.curIndex);
                        ++this.curIndex;
                        if (run.size() == UnorderedRunGenerator.this.maxBatchWriteSize) {
                            return this.runs.remove(run.type);
                        }
                    }
                    return this.runs.remove(this.runs.keySet().iterator().next());
                }
                
                public void remove() {
                    throw new UnsupportedOperationException("Not implemented");
                }
            };
        }
    }
    
    private class Run
    {
        private final List<WriteRequest> writeRequests;
        private final WriteRequest.Type type;
        private final WriteConcern writeConcern;
        private final DBEncoder encoder;
        private IndexMap indexMap;
        
        Run(final WriteRequest.Type type, final WriteConcern writeConcern, final DBEncoder encoder) {
            super();
            this.writeRequests = new ArrayList<WriteRequest>();
            this.type = type;
            this.indexMap = IndexMap.create();
            this.writeConcern = writeConcern;
            this.encoder = encoder;
        }
        
        void add(final WriteRequest writeRequest, final int originalIndex) {
            this.indexMap = this.indexMap.add(this.writeRequests.size(), originalIndex);
            this.writeRequests.add(writeRequest);
        }
        
        public int size() {
            return this.writeRequests.size();
        }
        
        BulkWriteResult execute(final DBPort port) {
            if (this.type == WriteRequest.Type.UPDATE) {
                return this.executeUpdates(this.getWriteRequestsAsModifyRequests(), port);
            }
            if (this.type == WriteRequest.Type.REPLACE) {
                return this.executeReplaces(this.getWriteRequestsAsModifyRequests(), port);
            }
            if (this.type == WriteRequest.Type.INSERT) {
                return this.executeInserts(this.getWriteRequestsAsInsertRequests(), port);
            }
            if (this.type == WriteRequest.Type.REMOVE) {
                return this.executeRemoves(this.getWriteRequestsAsRemoveRequests(), port);
            }
            throw new MongoInternalException(String.format("Unsupported write of type %s", this.type));
        }
        
        private List getWriteRequestsAsRaw() {
            return this.writeRequests;
        }
        
        private List<RemoveRequest> getWriteRequestsAsRemoveRequests() {
            return (List<RemoveRequest>)this.getWriteRequestsAsRaw();
        }
        
        private List<InsertRequest> getWriteRequestsAsInsertRequests() {
            return (List<InsertRequest>)this.getWriteRequestsAsRaw();
        }
        
        private List<ModifyRequest> getWriteRequestsAsModifyRequests() {
            return (List<ModifyRequest>)this.getWriteRequestsAsRaw();
        }
        
        BulkWriteResult executeUpdates(final List<ModifyRequest> updateRequests, final DBPort port) {
            for (final ModifyRequest request : updateRequests) {
                for (final String key : request.getUpdateDocument().keySet()) {
                    if (!key.startsWith("$")) {
                        throw new IllegalArgumentException("Update document keys must start with $: " + key);
                    }
                }
            }
            return new RunExecutor(port) {
                BulkWriteResult executeWriteCommandProtocol() {
                    return DBCollectionImpl.this.updateWithCommandProtocol(updateRequests, Run.this.writeConcern, Run.this.encoder, port);
                }
                
                WriteResult executeWriteProtocol(final int i) {
                    final ModifyRequest update = updateRequests.get(i);
                    final WriteResult writeResult = DBCollectionImpl.this.update(update.getQuery(), update.getUpdateDocument(), update.isUpsert(), update.isMulti(), Run.this.writeConcern, Run.this.encoder);
                    return this.addMissingUpserted(update, writeResult);
                }
                
                WriteRequest.Type getType() {
                    return WriteRequest.Type.UPDATE;
                }
            }.execute();
        }
        
        BulkWriteResult executeReplaces(final List<ModifyRequest> replaceRequests, final DBPort port) {
            for (final ModifyRequest request : replaceRequests) {
                DBCollectionImpl.this._checkObject(request.getUpdateDocument(), false, false);
            }
            return new RunExecutor(port) {
                BulkWriteResult executeWriteCommandProtocol() {
                    return DBCollectionImpl.this.updateWithCommandProtocol(replaceRequests, Run.this.writeConcern, Run.this.encoder, port);
                }
                
                WriteResult executeWriteProtocol(final int i) {
                    final ModifyRequest update = replaceRequests.get(i);
                    final WriteResult writeResult = DBCollectionImpl.this.update(update.getQuery(), update.getUpdateDocument(), update.isUpsert(), update.isMulti(), Run.this.writeConcern, Run.this.encoder);
                    return this.addMissingUpserted(update, writeResult);
                }
                
                WriteRequest.Type getType() {
                    return WriteRequest.Type.REPLACE;
                }
            }.execute();
        }
        
        BulkWriteResult executeRemoves(final List<RemoveRequest> removeRequests, final DBPort port) {
            return new RunExecutor(port) {
                BulkWriteResult executeWriteCommandProtocol() {
                    return DBCollectionImpl.this.removeWithCommandProtocol(removeRequests, Run.this.writeConcern, Run.this.encoder, port);
                }
                
                WriteResult executeWriteProtocol(final int i) {
                    final RemoveRequest removeRequest = removeRequests.get(i);
                    return DBCollectionImpl.this.remove(removeRequest.getQuery(), removeRequest.isMulti(), Run.this.writeConcern, Run.this.encoder);
                }
                
                WriteRequest.Type getType() {
                    return WriteRequest.Type.REMOVE;
                }
            }.execute();
        }
        
        BulkWriteResult executeInserts(final List<InsertRequest> insertRequests, final DBPort port) {
            return new RunExecutor(port) {
                BulkWriteResult executeWriteCommandProtocol() {
                    final List<DBObject> documents = new ArrayList<DBObject>(insertRequests.size());
                    for (final InsertRequest cur : insertRequests) {
                        documents.add(cur.getDocument());
                    }
                    return DBCollectionImpl.this.insertWithCommandProtocol(documents, Run.this.writeConcern, Run.this.encoder, port, true);
                }
                
                WriteResult executeWriteProtocol(final int i) {
                    return DBCollectionImpl.this.insert(Arrays.asList(insertRequests.get(i).getDocument()), Run.this.writeConcern, Run.this.encoder);
                }
                
                WriteRequest.Type getType() {
                    return WriteRequest.Type.INSERT;
                }
            }.execute();
        }
        
        private abstract class RunExecutor
        {
            private final DBPort port;
            
            RunExecutor(final DBPort port) {
                super();
                this.port = port;
            }
            
            abstract BulkWriteResult executeWriteCommandProtocol();
            
            abstract WriteResult executeWriteProtocol(final int p0);
            
            abstract WriteRequest.Type getType();
            
            BulkWriteResult getResult(final WriteResult writeResult) {
                final int count = this.getCount(writeResult);
                final List<BulkWriteUpsert> upsertedItems = this.getUpsertedItems(writeResult);
                final Integer modifiedCount = (this.getType() == WriteRequest.Type.UPDATE || this.getType() == WriteRequest.Type.REPLACE) ? null : 0;
                return new AcknowledgedBulkWriteResult(this.getType(), count - upsertedItems.size(), modifiedCount, upsertedItems);
            }
            
            BulkWriteResult execute() {
                if (DBCollectionImpl.this.useWriteCommands(Run.this.writeConcern, this.port)) {
                    return this.executeWriteCommandProtocol();
                }
                return this.executeWriteProtocol();
            }
            
            private BulkWriteResult executeWriteProtocol() {
                final BulkWriteBatchCombiner bulkWriteBatchCombiner = new BulkWriteBatchCombiner(this.port.getAddress(), Run.this.writeConcern);
                for (int i = 0; i < Run.this.writeRequests.size(); ++i) {
                    final IndexMap indexMap = IndexMap.create(i, 1);
                    try {
                        final WriteResult writeResult = this.executeWriteProtocol(i);
                        if (Run.this.writeConcern.callGetLastError()) {
                            bulkWriteBatchCombiner.addResult(this.getResult(writeResult), indexMap);
                        }
                    }
                    catch (WriteConcernException writeException) {
                        if (this.isWriteConcernError(writeException.getCommandResult())) {
                            bulkWriteBatchCombiner.addResult(this.getResult(new WriteResult(writeException.getCommandResult(), Run.this.writeConcern)), indexMap);
                            bulkWriteBatchCombiner.addWriteConcernErrorResult(this.getWriteConcernError(writeException.getCommandResult()));
                        }
                        else {
                            bulkWriteBatchCombiner.addWriteErrorResult(this.getBulkWriteError(writeException), indexMap);
                        }
                        if (bulkWriteBatchCombiner.shouldStopSendingMoreBatches()) {
                            break;
                        }
                    }
                }
                return bulkWriteBatchCombiner.getResult();
            }
            
            private int getCount(final WriteResult writeResult) {
                return (this.getType() == WriteRequest.Type.INSERT) ? 1 : writeResult.getN();
            }
            
            List<BulkWriteUpsert> getUpsertedItems(final WriteResult writeResult) {
                return (writeResult.getUpsertedId() == null) ? Collections.emptyList() : Arrays.asList(new BulkWriteUpsert(0, writeResult.getUpsertedId()));
            }
            
            private BulkWriteError getBulkWriteError(final WriteConcernException writeException) {
                return new BulkWriteError(writeException.getCode(), writeException.getCommandResult().getString("err"), this.getErrorResponseDetails(writeException.getCommandResult()), 0);
            }
            
            private boolean isWriteConcernError(final CommandResult commandResult) {
                return commandResult.get("wtimeout") != null;
            }
            
            private WriteConcernError getWriteConcernError(final CommandResult commandResult) {
                return new WriteConcernError(commandResult.getCode(), this.getWriteConcernErrorMessage(commandResult), this.getErrorResponseDetails(commandResult));
            }
            
            private String getWriteConcernErrorMessage(final CommandResult commandResult) {
                return commandResult.getString("err");
            }
            
            private DBObject getErrorResponseDetails(final DBObject response) {
                final DBObject details = new BasicDBObject();
                for (final String key : response.keySet()) {
                    if (!Arrays.asList("ok", "err", "code").contains(key)) {
                        details.put(key, response.get(key));
                    }
                }
                return details;
            }
            
            WriteResult addMissingUpserted(final ModifyRequest update, final WriteResult writeResult) {
                if (update.isUpsert() && Run.this.writeConcern.callGetLastError() && !writeResult.isUpdateOfExisting() && writeResult.getUpsertedId() == null) {
                    final DBObject updateDocument = update.getUpdateDocument();
                    final DBObject query = update.getQuery();
                    if (updateDocument.containsField("_id")) {
                        final CommandResult commandResult = writeResult.getLastError();
                        commandResult.put("upserted", updateDocument.get("_id"));
                        return new WriteResult(commandResult, writeResult.getLastConcern());
                    }
                    if (query.containsField("_id")) {
                        final CommandResult commandResult = writeResult.getLastError();
                        commandResult.put("upserted", query.get("_id"));
                        return new WriteResult(commandResult, writeResult.getLastConcern());
                    }
                }
                return writeResult;
            }
        }
    }
}
