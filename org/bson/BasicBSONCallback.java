package org.bson;

import java.util.regex.*;
import java.util.*;
import org.bson.types.*;

public class BasicBSONCallback implements BSONCallback
{
    private Object _root;
    private final LinkedList<BSONObject> _stack;
    private final LinkedList<String> _nameStack;
    
    public BasicBSONCallback() {
        super();
        this._stack = new LinkedList<BSONObject>();
        this._nameStack = new LinkedList<String>();
        this.reset();
    }
    
    public BSONObject create() {
        return new BasicBSONObject();
    }
    
    protected BSONObject createList() {
        return new BasicBSONList();
    }
    
    public BSONCallback createBSONCallback() {
        return new BasicBSONCallback();
    }
    
    public BSONObject create(final boolean array, final List<String> path) {
        if (array) {
            return this.createList();
        }
        return this.create();
    }
    
    public void objectStart() {
        if (this._stack.size() > 0) {
            throw new IllegalStateException("something is wrong");
        }
        this.objectStart(false);
    }
    
    @Deprecated
    public void objectStart(final boolean array) {
        this._root = this.create(array, null);
        this._stack.add((BSONObject)this._root);
    }
    
    public void objectStart(final String name) {
        this.objectStart(false, name);
    }
    
    @Deprecated
    public void objectStart(final boolean array, final String name) {
        this._nameStack.addLast(name);
        final BSONObject o = this.create(array, this._nameStack);
        this._stack.getLast().put(name, o);
        this._stack.addLast(o);
    }
    
    public Object objectDone() {
        final BSONObject o = this._stack.removeLast();
        if (this._nameStack.size() > 0) {
            this._nameStack.removeLast();
        }
        else if (this._stack.size() > 0) {
            throw new IllegalStateException("something is wrong");
        }
        return BSON.hasDecodeHooks() ? BSON.applyDecodingHooks(o) : o;
    }
    
    public void arrayStart() {
        this.objectStart(true);
    }
    
    public void arrayStart(final String name) {
        this.objectStart(true, name);
    }
    
    public Object arrayDone() {
        return this.objectDone();
    }
    
    public void gotNull(final String name) {
        this.cur().put(name, null);
    }
    
    public void gotUndefined(final String name) {
    }
    
    public void gotMinKey(final String name) {
        this.cur().put(name, new MinKey());
    }
    
    public void gotMaxKey(final String name) {
        this.cur().put(name, new MaxKey());
    }
    
    public void gotBoolean(final String name, final boolean value) {
        this._put(name, value);
    }
    
    public void gotDouble(final String name, final double value) {
        this._put(name, value);
    }
    
    public void gotInt(final String name, final int value) {
        this._put(name, value);
    }
    
    public void gotLong(final String name, final long value) {
        this._put(name, value);
    }
    
    public void gotDate(final String name, final long millis) {
        this._put(name, new Date(millis));
    }
    
    public void gotRegex(final String name, final String pattern, final String flags) {
        this._put(name, Pattern.compile(pattern, BSON.regexFlags(flags)));
    }
    
    public void gotString(final String name, final String value) {
        this._put(name, value);
    }
    
    public void gotSymbol(final String name, final String value) {
        this._put(name, value);
    }
    
    public void gotTimestamp(final String name, final int time, final int inc) {
        this._put(name, new BSONTimestamp(time, inc));
    }
    
    public void gotObjectId(final String name, final ObjectId id) {
        this._put(name, id);
    }
    
    public void gotDBRef(final String name, final String ns, final ObjectId id) {
        this._put(name, new BasicBSONObject("$ns", ns).append("$id", id));
    }
    
    @Deprecated
    public void gotBinaryArray(final String name, final byte[] data) {
        this.gotBinary(name, (byte)0, data);
    }
    
    public void gotBinary(final String name, final byte type, final byte[] data) {
        if (type == 0 || type == 2) {
            this._put(name, data);
        }
        else {
            this._put(name, new Binary(type, data));
        }
    }
    
    public void gotUUID(final String name, final long part1, final long part2) {
        this._put(name, new UUID(part1, part2));
    }
    
    public void gotCode(final String name, final String code) {
        this._put(name, new Code(code));
    }
    
    public void gotCodeWScope(final String name, final String code, final Object scope) {
        this._put(name, new CodeWScope(code, (BSONObject)scope));
    }
    
    protected void _put(final String name, final Object o) {
        this.cur().put(name, BSON.hasDecodeHooks() ? BSON.applyDecodingHooks(o) : o);
    }
    
    protected BSONObject cur() {
        return this._stack.getLast();
    }
    
    protected String curName() {
        return this._nameStack.isEmpty() ? null : this._nameStack.getLast();
    }
    
    public Object get() {
        return this._root;
    }
    
    protected void setRoot(final Object o) {
        this._root = o;
    }
    
    protected boolean isStackEmpty() {
        return this._stack.size() < 1;
    }
    
    public void reset() {
        this._root = null;
        this._stack.clear();
        this._nameStack.clear();
    }
}
