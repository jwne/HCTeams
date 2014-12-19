package com.mongodb;

import java.util.concurrent.atomic.*;
import org.bson.io.*;
import org.bson.*;
import java.io.*;

class OutMessage extends BasicBSONEncoder
{
    static AtomicInteger REQUEST_ID;
    private final Mongo _mongo;
    private final DBCollection _collection;
    private PoolOutputBuffer _buffer;
    private final int _id;
    private final OpCode _opCode;
    private final int _queryOptions;
    private final DBObject _query;
    private final DBEncoder _encoder;
    private final int _maxBSONObjectSize;
    private volatile int _numDocuments;
    
    public static OutMessage insert(final DBCollection collection, final DBEncoder encoder, final WriteConcern concern) {
        final OutMessage om = new OutMessage(collection, OpCode.OP_INSERT, encoder);
        om.writeInsertPrologue(concern);
        return om;
    }
    
    public static OutMessage update(final DBCollection collection, final DBEncoder encoder, final boolean upsert, final boolean multi, final DBObject query, final DBObject o) {
        final OutMessage om = new OutMessage(collection, OpCode.OP_UPDATE, encoder, query);
        om.writeUpdate(upsert, multi, query, o);
        return om;
    }
    
    public static OutMessage remove(final DBCollection collection, final DBEncoder encoder, final DBObject query, final boolean multi) {
        final OutMessage om = new OutMessage(collection, OpCode.OP_DELETE, encoder, query);
        om.writeRemove(multi);
        return om;
    }
    
    static OutMessage query(final DBCollection collection, final int options, final int numToSkip, final int batchSize, final DBObject query, final DBObject fields, final int maxBSONObjectSize) {
        return query(collection, options, numToSkip, batchSize, query, fields, ReadPreference.primary(), DefaultDBEncoder.FACTORY.create(), maxBSONObjectSize);
    }
    
    static OutMessage query(final DBCollection collection, final int options, final int numToSkip, final int batchSize, final DBObject query, final DBObject fields, final ReadPreference readPref, final DBEncoder enc) {
        return query(collection, options, numToSkip, batchSize, query, fields, readPref, enc, 0);
    }
    
    static OutMessage query(final DBCollection collection, final int options, final int numToSkip, final int batchSize, final DBObject query, final DBObject fields, final ReadPreference readPref, final DBEncoder enc, final int maxBSONObjectSize) {
        final OutMessage om = new OutMessage(collection, enc, query, options, readPref, maxBSONObjectSize);
        om.writeQuery(fields, numToSkip, batchSize);
        return om;
    }
    
    static OutMessage getMore(final DBCollection collection, final long cursorId, final int batchSize) {
        final OutMessage om = new OutMessage(collection, OpCode.OP_GETMORE);
        om.writeGetMore(cursorId, batchSize);
        return om;
    }
    
    static OutMessage killCursors(final Mongo mongo, final int numCursors) {
        final OutMessage om = new OutMessage(mongo, OpCode.OP_KILL_CURSORS);
        om.writeKillCursorsPrologue(numCursors);
        return om;
    }
    
    private OutMessage(final Mongo m, final OpCode opCode) {
        this(null, m, opCode, null);
    }
    
    private OutMessage(final DBCollection collection, final OpCode opCode) {
        this(collection, opCode, null);
    }
    
    private OutMessage(final DBCollection collection, final OpCode opCode, final DBEncoder enc) {
        this(collection, collection.getDB().getMongo(), opCode, enc);
    }
    
    private OutMessage(final DBCollection collection, final Mongo m, final OpCode opCode, final DBEncoder enc) {
        this(collection, m, opCode, enc, null, -1, null, 0);
    }
    
    private OutMessage(final DBCollection collection, final OpCode opCode, final DBEncoder enc, final DBObject query) {
        this(collection, collection.getDB().getMongo(), opCode, enc, query, 0, null, 0);
    }
    
    private OutMessage(final DBCollection collection, final DBEncoder enc, final DBObject query, final int options, final ReadPreference readPref, final int maxBSONObjectSize) {
        this(collection, collection.getDB().getMongo(), OpCode.OP_QUERY, enc, query, options, readPref, maxBSONObjectSize);
    }
    
    private OutMessage(final DBCollection collection, final Mongo m, final OpCode opCode, final DBEncoder enc, final DBObject query, final int options, final ReadPreference readPref, final int maxBSONObjectSize) {
        super();
        this._collection = collection;
        this._mongo = m;
        this._encoder = enc;
        this._maxBSONObjectSize = maxBSONObjectSize;
        (this._buffer = this._mongo._bufferPool.get()).reset();
        this.set(this._buffer);
        this._id = OutMessage.REQUEST_ID.getAndIncrement();
        this.writeMessagePrologue(this._opCode = opCode);
        if (query == null) {
            this._query = null;
            this._queryOptions = 0;
        }
        else {
            this._query = query;
            int allOptions = options;
            if (readPref != null && readPref.isSlaveOk()) {
                allOptions |= 0x4;
            }
            this._queryOptions = allOptions;
        }
    }
    
    private void writeInsertPrologue(final WriteConcern concern) {
        int flags = 0;
        if (concern.getContinueOnErrorForInsert()) {
            flags |= 0x1;
        }
        this.writeInt(flags);
        this.writeCString(this._collection.getFullName());
    }
    
    private void writeUpdate(final boolean upsert, final boolean multi, final DBObject query, final DBObject o) {
        this.writeInt(0);
        this.writeCString(this._collection.getFullName());
        int flags = 0;
        if (upsert) {
            flags |= 0x1;
        }
        if (multi) {
            flags |= 0x2;
        }
        this.writeInt(flags);
        this.putObject(query);
        this.putObject(o);
    }
    
    private void writeRemove(final boolean multi) {
        this.writeInt(0);
        this.writeCString(this._collection.getFullName());
        this.writeInt(multi ? 0 : 1);
        this.putObject(this._query);
    }
    
    private void writeGetMore(final long cursorId, final int batchSize) {
        this.writeInt(0);
        this.writeCString(this._collection.getFullName());
        this.writeInt(batchSize);
        this.writeLong(cursorId);
    }
    
    private void writeKillCursorsPrologue(final int numCursors) {
        this.writeInt(0);
        this.writeInt(numCursors);
    }
    
    private void writeQuery(final DBObject fields, final int numToSkip, final int batchSize) {
        this.writeInt(this._queryOptions);
        this.writeCString(this._collection.getFullName());
        this.writeInt(numToSkip);
        this.writeInt(batchSize);
        this.putObject(this._query);
        if (fields != null) {
            this.putObject(fields);
        }
    }
    
    private void writeMessagePrologue(final OpCode opCode) {
        this.writeInt(0);
        this.writeInt(this._id);
        this.writeInt(0);
        this.writeInt(opCode.getValue());
    }
    
    void prepare() {
        if (this._buffer == null) {
            throw new IllegalStateException("Already closed");
        }
        this._buffer.writeInt(0, this._buffer.size());
    }
    
    void pipe(final OutputStream out) throws IOException {
        if (this._buffer == null) {
            throw new IllegalStateException("Already closed");
        }
        this._buffer.pipe(out);
    }
    
    int size() {
        if (this._buffer == null) {
            throw new IllegalStateException("Already closed");
        }
        return this._buffer.size();
    }
    
    void doneWithMessage() {
        if (this._buffer == null) {
            throw new IllegalStateException("Only call this once per instance");
        }
        this._buffer.reset();
        this._mongo._bufferPool.done(this._buffer);
        this._buffer = null;
        this.done();
    }
    
    boolean hasOption(final int option) {
        return (this._queryOptions & option) != 0x0;
    }
    
    int getId() {
        return this._id;
    }
    
    OpCode getOpCode() {
        return this._opCode;
    }
    
    DBObject getQuery() {
        return this._query;
    }
    
    String getNamespace() {
        return (this._collection != null) ? this._collection.getFullName() : null;
    }
    
    int getNumDocuments() {
        return this._numDocuments;
    }
    
    public int putObject(final BSONObject o) {
        if (this._buffer == null) {
            throw new IllegalStateException("Already closed");
        }
        final int objectSize = this._encoder.writeObject(this._buf, o);
        if (objectSize > Math.max((this._maxBSONObjectSize != 0) ? this._maxBSONObjectSize : this._mongo.getMaxBsonObjectSize(), 4194304)) {
            throw new MongoInternalException("DBObject of size " + objectSize + " is over Max BSON size " + this._mongo.getMaxBsonObjectSize());
        }
        ++this._numDocuments;
        return objectSize;
    }
    
    static {
        OutMessage.REQUEST_ID = new AtomicInteger(1);
    }
    
    enum OpCode
    {
        OP_UPDATE(2001), 
        OP_INSERT(2002), 
        OP_QUERY(2004), 
        OP_GETMORE(2005), 
        OP_DELETE(2006), 
        OP_KILL_CURSORS(2007);
        
        private final int value;
        
        private OpCode(final int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
}
