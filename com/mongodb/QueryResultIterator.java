package com.mongodb;

import org.bson.*;
import java.util.*;

class QueryResultIterator implements Cursor
{
    private final DBApiLayer _db;
    private final DBDecoder _decoder;
    private final DBCollectionImpl _collection;
    private final int _options;
    private final ServerAddress _host;
    private final int _limit;
    private long _cursorId;
    private Iterator<DBObject> _cur;
    private int _curSize;
    private int _batchSize;
    private boolean closed;
    private final List<Integer> _sizes;
    private int _numGetMores;
    private int _numFetched;
    private OptionalFinalizer _optionalFinalizer;
    private boolean batchSizeTrackingDisabled;
    
    QueryResultIterator(final DBApiLayer db, final DBCollectionImpl collection, final Response res, final int batchSize, final int limit, final int options, final DBDecoder decoder) {
        super();
        this._sizes = new ArrayList<Integer>();
        this._numGetMores = 0;
        this._numFetched = 0;
        this._db = db;
        this._collection = collection;
        this._batchSize = batchSize;
        this._limit = limit;
        this._options = options;
        this._host = res._host;
        this._decoder = decoder;
        this.initFromQueryResponse(res);
    }
    
    QueryResultIterator(final DBObject cursorDocument, final DBApiLayer db, final DBCollectionImpl collection, final int batchSize, final DBDecoder decoder, final ServerAddress serverAddress) {
        super();
        this._sizes = new ArrayList<Integer>();
        this._numGetMores = 0;
        this._numFetched = 0;
        this._db = db;
        this._collection = collection;
        this._batchSize = batchSize;
        this._host = serverAddress;
        this._limit = 0;
        this._options = 0;
        this._decoder = decoder;
        this.initFromCursorDocument(cursorDocument);
    }
    
    static int chooseBatchSize(final int batchSize, final int limit, final int fetched) {
        final int bs = Math.abs(batchSize);
        final int remaining = (limit > 0) ? (limit - fetched) : 0;
        int res;
        if (bs == 0 && remaining > 0) {
            res = remaining;
        }
        else if (bs > 0 && remaining == 0) {
            res = bs;
        }
        else {
            res = Math.min(bs, remaining);
        }
        if (batchSize < 0) {
            res = -res;
        }
        if (res == 1) {
            res = -1;
        }
        return res;
    }
    
    public DBObject next() {
        if (this.closed) {
            throw new IllegalStateException("Iterator has been closed");
        }
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return this._cur.next();
    }
    
    public boolean tryHasNext() {
        if (this.closed) {
            throw new IllegalStateException("Iterator has been closed");
        }
        if (this._cur.hasNext()) {
            return true;
        }
        if (this._cursorId != 0L) {
            this.getMore();
        }
        return this._curSize > 0;
    }
    
    public boolean hasNext() {
        if (this.closed) {
            throw new IllegalStateException("Iterator has been closed");
        }
        if (this._cur.hasNext()) {
            return true;
        }
        while (this._cursorId != 0L) {
            this.getMore();
            if (this._curSize > 0) {
                return true;
            }
        }
        return false;
    }
    
    private void getMore() {
        final Response res = this._db._connector.call(this._collection.getDB(), this._collection, OutMessage.getMore(this._collection, this._cursorId, this.getGetMoreBatchSize()), this._host, this._decoder);
        ++this._numGetMores;
        this.initFromQueryResponse(res);
    }
    
    private int getGetMoreBatchSize() {
        return chooseBatchSize(this._batchSize, this._limit, this._numFetched);
    }
    
    public void remove() {
        throw new UnsupportedOperationException("can't remove a document via a query result iterator");
    }
    
    public void setBatchSize(final int size) {
        this._batchSize = size;
    }
    
    public long getCursorId() {
        return this._cursorId;
    }
    
    int numGetMores() {
        return this._numGetMores;
    }
    
    List<Integer> getSizes() {
        return Collections.unmodifiableList((List<? extends Integer>)this._sizes);
    }
    
    public void close() {
        if (!this.closed) {
            this.closed = true;
            this.killCursor();
        }
    }
    
    private void initFromQueryResponse(final Response response) {
        this.init(response._flags, response.cursor(), response.size(), response.iterator());
    }
    
    private void initFromCursorDocument(final DBObject cursorDocument) {
        final Map cursor = (Map)cursorDocument.get("cursor");
        if (cursor != null) {
            final long cursorId = cursor.get("id");
            final List<DBObject> firstBatch = cursor.get("firstBatch");
            this.init(0, cursorId, firstBatch.size(), firstBatch.iterator());
        }
        else {
            final List<DBObject> result = (List<DBObject>)cursorDocument.get("result");
            this.init(0, 0L, result.size(), result.iterator());
        }
    }
    
    private void init(final int flags, final long cursorId, final int size, final Iterator<DBObject> iterator) {
        this._curSize = size;
        this._cur = iterator;
        if (!this.batchSizeTrackingDisabled) {
            this._sizes.add(size);
        }
        this._numFetched += size;
        if (this._optionalFinalizer == null) {
            this._optionalFinalizer = this.createFinalizerIfNeeded(cursorId);
        }
        this.setCursorIdOnFinalizer(cursorId);
        this.throwOnQueryFailure(this._cursorId, flags);
        this._cursorId = cursorId;
        if (cursorId != 0L && this._limit > 0 && this._limit - this._numFetched <= 0) {
            this.killCursor();
        }
    }
    
    private void setCursorIdOnFinalizer(final long cursorId) {
        if (this._optionalFinalizer != null) {
            this._optionalFinalizer.setCursorId(cursorId);
        }
    }
    
    private void throwOnQueryFailure(final long cursorId, final int flags) {
        if ((flags & 0x2) > 0) {
            final BSONObject errorDocument = this._cur.next();
            if (ServerError.getCode(errorDocument) == 50) {
                throw new MongoExecutionTimeoutException(ServerError.getCode(errorDocument), ServerError.getMsg(errorDocument, null));
            }
            throw new MongoException(ServerError.getCode(errorDocument), ServerError.getMsg(errorDocument, null));
        }
        else if ((flags & 0x1) > 0) {
            throw new MongoException.CursorNotFound(cursorId, this._host);
        }
    }
    
    void killCursor() {
        this.setCursorIdOnFinalizer(0L);
        if (this._cursorId == 0L) {
            return;
        }
        try {
            this._db.killCursors(this._host, Arrays.asList(this._cursorId));
            this._cursorId = 0L;
        }
        catch (MongoException e) {
            this._db.addDeadCursor(new DBApiLayer.DeadCursor(this._cursorId, this._host));
        }
    }
    
    public ServerAddress getServerAddress() {
        return this._host;
    }
    
    void disableBatchSizeTracking() {
        this.batchSizeTrackingDisabled = true;
        this._sizes.clear();
    }
    
    boolean hasFinalizer() {
        return this._optionalFinalizer != null;
    }
    
    private OptionalFinalizer createFinalizerIfNeeded(final long cursorId) {
        return (this._collection.getDB().getMongo().getMongoOptions().isCursorFinalizerEnabled() && cursorId != 0L) ? new OptionalFinalizer(this._db, this._host) : null;
    }
    
    private static class OptionalFinalizer
    {
        private final DBApiLayer db;
        private final ServerAddress host;
        private volatile long cursorId;
        
        private OptionalFinalizer(final DBApiLayer db, final ServerAddress host) {
            super();
            this.db = db;
            this.host = host;
        }
        
        public void setCursorId(final long cursorId) {
            this.cursorId = cursorId;
        }
        
        protected void finalize() throws Throwable {
            if (this.cursorId != 0L) {
                this.db.addDeadCursor(new DBApiLayer.DeadCursor(this.cursorId, this.host));
            }
        }
    }
}
