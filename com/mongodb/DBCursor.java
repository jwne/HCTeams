package com.mongodb;

import org.bson.util.annotations.*;
import java.util.concurrent.*;
import java.util.*;

@NotThreadSafe
public class DBCursor implements Cursor, Iterable<DBObject>
{
    private final DBCollection _collection;
    private final DBObject _query;
    private final DBObject _keysWanted;
    private DBObject _orderBy;
    private String _hint;
    private DBObject _hintDBObj;
    private boolean _explain;
    private int _limit;
    private int _batchSize;
    private int _skip;
    private boolean _snapshot;
    private int _options;
    private long _maxTimeMS;
    private ReadPreference _readPref;
    private DBDecoderFactory _decoderFact;
    private DBObject _specialFields;
    private QueryResultIterator _it;
    private CursorType _cursorType;
    private DBObject _cur;
    private int _num;
    private boolean _disableBatchSizeTracking;
    private final ArrayList<DBObject> _all;
    
    public DBCursor(final DBCollection collection, final DBObject q, final DBObject k, final ReadPreference preference) {
        super();
        this._orderBy = null;
        this._hint = null;
        this._hintDBObj = null;
        this._explain = false;
        this._limit = 0;
        this._batchSize = 0;
        this._skip = 0;
        this._snapshot = false;
        this._options = 0;
        this._it = null;
        this._cursorType = null;
        this._cur = null;
        this._num = 0;
        this._all = new ArrayList<DBObject>();
        if (collection == null) {
            throw new IllegalArgumentException("collection is null");
        }
        this._collection = collection;
        this._query = ((q == null) ? new BasicDBObject() : q);
        this._keysWanted = k;
        this._options = this._collection.getOptions();
        this._readPref = preference;
        this._decoderFact = collection.getDBDecoderFactory();
    }
    
    public DBCursor comment(final String comment) {
        this.addSpecial("$comment", comment);
        return this;
    }
    
    public DBCursor maxScan(final int max) {
        this.addSpecial("$maxScan", max);
        return this;
    }
    
    public DBCursor max(final DBObject max) {
        this.addSpecial("$max", max);
        return this;
    }
    
    public DBCursor min(final DBObject min) {
        this.addSpecial("$min", min);
        return this;
    }
    
    public DBCursor returnKey() {
        this.addSpecial("$returnKey", true);
        return this;
    }
    
    public DBCursor showDiskLoc() {
        this.addSpecial("$showDiskLoc", true);
        return this;
    }
    
    public DBCursor copy() {
        final DBCursor c = new DBCursor(this._collection, this._query, this._keysWanted, this._readPref);
        c._orderBy = this._orderBy;
        c._hint = this._hint;
        c._hintDBObj = this._hintDBObj;
        c._limit = this._limit;
        c._skip = this._skip;
        c._options = this._options;
        c._batchSize = this._batchSize;
        c._snapshot = this._snapshot;
        c._explain = this._explain;
        c._maxTimeMS = this._maxTimeMS;
        c._disableBatchSizeTracking = this._disableBatchSizeTracking;
        if (this._specialFields != null) {
            c._specialFields = new BasicDBObject(this._specialFields.toMap());
        }
        return c;
    }
    
    public Iterator<DBObject> iterator() {
        return this.copy();
    }
    
    public DBCursor sort(final DBObject orderBy) {
        if (this._it != null) {
            throw new IllegalStateException("can't sort after executing query");
        }
        this._orderBy = orderBy;
        return this;
    }
    
    public DBCursor addSpecial(final String name, final Object o) {
        if (this._specialFields == null) {
            this._specialFields = new BasicDBObject();
        }
        this._specialFields.put(name, o);
        return this;
    }
    
    public DBCursor hint(final DBObject indexKeys) {
        if (this._it != null) {
            throw new IllegalStateException("can't hint after executing query");
        }
        this._hintDBObj = indexKeys;
        return this;
    }
    
    public DBCursor hint(final String indexName) {
        if (this._it != null) {
            throw new IllegalStateException("can't hint after executing query");
        }
        this._hint = indexName;
        return this;
    }
    
    public DBCursor maxTime(final long maxTime, final TimeUnit timeUnit) {
        this._maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }
    
    public DBCursor snapshot() {
        if (this._it != null) {
            throw new IllegalStateException("can't snapshot after executing the query");
        }
        this._snapshot = true;
        return this;
    }
    
    public DBObject explain() {
        final DBCursor c = this.copy();
        c._explain = true;
        if (c._limit > 0) {
            c._batchSize = c._limit * -1;
            c._limit = 0;
        }
        return c.next();
    }
    
    public DBCursor limit(final int limit) {
        if (this._it != null) {
            throw new IllegalStateException("can't set limit after executing query");
        }
        if (limit > 0) {
            this._limit = limit;
        }
        else if (limit < 0) {
            this.batchSize(limit);
        }
        return this;
    }
    
    public DBCursor batchSize(int numberOfElements) {
        if (numberOfElements == 1) {
            numberOfElements = 2;
        }
        if (this._it != null) {
            this._it.setBatchSize(numberOfElements);
        }
        this._batchSize = numberOfElements;
        return this;
    }
    
    public DBCursor skip(final int numberOfElements) {
        if (this._it != null) {
            throw new IllegalStateException("can't set skip after executing query");
        }
        this._skip = numberOfElements;
        return this;
    }
    
    public long getCursorId() {
        return (this._it == null) ? 0L : this._it.getCursorId();
    }
    
    public void close() {
        if (this._it != null) {
            this._it.close();
        }
    }
    
    @Deprecated
    public DBCursor slaveOk() {
        return this.addOption(4);
    }
    
    public DBCursor addOption(final int option) {
        this.setOptions(this._options |= option);
        return this;
    }
    
    public DBCursor setOptions(final int options) {
        if ((options & 0x40) != 0x0) {
            throw new IllegalArgumentException("The exhaust option is not user settable.");
        }
        this._options = options;
        return this;
    }
    
    public DBCursor resetOptions() {
        this._options = 0;
        return this;
    }
    
    public int getOptions() {
        return this._options;
    }
    
    public int getLimit() {
        return this._limit;
    }
    
    public int getBatchSize() {
        return this._batchSize;
    }
    
    private void _check() {
        if (this._it != null) {
            return;
        }
        this._lookForHints();
        final QueryOpBuilder builder = new QueryOpBuilder().addQuery(this._query).addOrderBy(this._orderBy).addHint(this._hintDBObj).addHint(this._hint).addExplain(this._explain).addSnapshot(this._snapshot).addSpecialFields(this._specialFields).addMaxTimeMS(this._maxTimeMS);
        if (this._collection.getDB().getMongo().isMongosConnection()) {
            builder.addReadPreference(this._readPref);
        }
        this._it = this._collection.find(builder.get(), this._keysWanted, this._skip, this._batchSize, this._limit, this._options, this._readPref, this.getDecoder());
        if (this._disableBatchSizeTracking) {
            this._it.disableBatchSizeTracking();
        }
    }
    
    private DBDecoder getDecoder() {
        return (this._decoderFact != null) ? this._decoderFact.create() : null;
    }
    
    private void _lookForHints() {
        if (this._hint != null) {
            return;
        }
        if (this._collection._hintFields == null) {
            return;
        }
        final Set<String> mykeys = this._query.keySet();
        for (final DBObject o : this._collection._hintFields) {
            final Set<String> hintKeys = o.keySet();
            if (!mykeys.containsAll(hintKeys)) {
                continue;
            }
            this.hint(o);
        }
    }
    
    void _checkType(final CursorType type) {
        if (this._cursorType == null) {
            this._cursorType = type;
            return;
        }
        if (type == this._cursorType) {
            return;
        }
        throw new IllegalArgumentException("can't switch cursor access methods");
    }
    
    private DBObject _next() {
        if (this._cursorType == null) {
            this._checkType(CursorType.ITERATOR);
        }
        this._check();
        this._cur = this._it.next();
        ++this._num;
        if (this._keysWanted != null && this._keysWanted.keySet().size() > 0) {
            this._cur.markAsPartialObject();
        }
        if (this._cursorType == CursorType.ARRAY) {
            this._all.add(this._cur);
        }
        return this._cur;
    }
    
    @Deprecated
    public int numGetMores() {
        return (this._it == null) ? 0 : this._it.numGetMores();
    }
    
    @Deprecated
    public List<Integer> getSizes() {
        return (this._it == null) ? Collections.emptyList() : this._it.getSizes();
    }
    
    @Deprecated
    public DBCursor disableBatchSizeTracking() {
        if (this._it != null) {
            throw new IllegalStateException("can't disable batch size tracking after executing query");
        }
        this._disableBatchSizeTracking = true;
        return this;
    }
    
    @Deprecated
    public boolean isBatchSizeTrackingDisabled() {
        return this._disableBatchSizeTracking;
    }
    
    private boolean _hasNext() {
        this._check();
        return (this._limit <= 0 || this._num < this._limit) && this._it.hasNext();
    }
    
    public int numSeen() {
        return this._num;
    }
    
    public boolean hasNext() {
        this._checkType(CursorType.ITERATOR);
        if ((this.getOptions() & 0x2) != 0x0) {
            this.addOption(32);
        }
        return this._hasNext();
    }
    
    public DBObject tryNext() {
        this._checkType(CursorType.ITERATOR);
        if ((this.getOptions() & 0x2) != 0x2) {
            throw new IllegalArgumentException("Can only be used with a tailable cursor");
        }
        this._check();
        if (!this._it.tryHasNext()) {
            return null;
        }
        return this._next();
    }
    
    public DBObject next() {
        this._checkType(CursorType.ITERATOR);
        if ((this.getOptions() & 0x2) != 0x0) {
            this.addOption(32);
        }
        return this._next();
    }
    
    public DBObject curr() {
        this._checkType(CursorType.ITERATOR);
        return this._cur;
    }
    
    public void remove() {
        throw new UnsupportedOperationException("can't remove from a cursor");
    }
    
    void _fill(final int n) {
        this._checkType(CursorType.ARRAY);
        while (n >= this._all.size() && this._hasNext()) {
            this._next();
        }
    }
    
    public int length() {
        this._checkType(CursorType.ARRAY);
        this._fill(Integer.MAX_VALUE);
        return this._all.size();
    }
    
    public List<DBObject> toArray() {
        return this.toArray(Integer.MAX_VALUE);
    }
    
    public List<DBObject> toArray(final int max) {
        this._checkType(CursorType.ARRAY);
        this._fill(max - 1);
        return this._all;
    }
    
    public int itcount() {
        int n = 0;
        while (this.hasNext()) {
            this.next();
            ++n;
        }
        return n;
    }
    
    public int count() {
        Object hint = (this._hint != null) ? this._hint : this._hintDBObj;
        if (hint == null && this._specialFields != null && this._specialFields.containsField("$hint")) {
            hint = this._specialFields.get("$hint");
        }
        return (int)this._collection.getCount(this._query, this._keysWanted, 0L, 0L, this.getReadPreference(), this._maxTimeMS, TimeUnit.MILLISECONDS, hint);
    }
    
    public DBObject one() {
        return this._collection.findOne(this._query, this._keysWanted, this._orderBy, this.getReadPreference(), this._maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public int size() {
        return (int)this._collection.getCount(this._query, this._keysWanted, this._limit, this._skip, this.getReadPreference(), this._maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public DBObject getKeysWanted() {
        return this._keysWanted;
    }
    
    public DBObject getQuery() {
        return this._query;
    }
    
    public DBCollection getCollection() {
        return this._collection;
    }
    
    public ServerAddress getServerAddress() {
        return (this._it == null) ? null : this._it.getServerAddress();
    }
    
    public DBCursor setReadPreference(final ReadPreference readPreference) {
        this._readPref = readPreference;
        return this;
    }
    
    public ReadPreference getReadPreference() {
        return this._readPref;
    }
    
    public DBCursor setDecoderFactory(final DBDecoderFactory fact) {
        this._decoderFact = fact;
        return this;
    }
    
    public DBDecoderFactory getDecoderFactory() {
        return this._decoderFact;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Cursor id=").append(this.getCursorId());
        sb.append(", ns=").append(this.getCollection().getFullName());
        sb.append(", query=").append(this.getQuery());
        if (this.getKeysWanted() != null) {
            sb.append(", fields=").append(this.getKeysWanted());
        }
        sb.append(", numIterated=").append(this._num);
        if (this._skip != 0) {
            sb.append(", skip=").append(this._skip);
        }
        if (this._limit != 0) {
            sb.append(", limit=").append(this._limit);
        }
        if (this._batchSize != 0) {
            sb.append(", batchSize=").append(this._batchSize);
        }
        final ServerAddress addr = this.getServerAddress();
        if (addr != null) {
            sb.append(", addr=").append(addr);
        }
        if (this._readPref != null) {
            sb.append(", readPreference=").append(this._readPref.toString());
        }
        return sb.toString();
    }
    
    boolean hasFinalizer() {
        return this._it != null && this._it.hasFinalizer();
    }
    
    enum CursorType
    {
        ITERATOR, 
        ARRAY;
    }
}
