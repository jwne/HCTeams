package org.bson;

import com.mongodb.util.*;
import org.bson.types.*;
import java.util.*;

public class BasicBSONObject extends LinkedHashMap<String, Object> implements BSONObject
{
    private static final long serialVersionUID = -4415279469780082174L;
    
    public BasicBSONObject() {
        super();
    }
    
    public BasicBSONObject(final int size) {
        super(size);
    }
    
    public BasicBSONObject(final String key, final Object value) {
        super();
        this.put(key, value);
    }
    
    public BasicBSONObject(final Map m) {
        super(m);
    }
    
    public Map toMap() {
        return new LinkedHashMap(this);
    }
    
    public Object removeField(final String key) {
        return ((HashMap<K, Object>)this).remove(key);
    }
    
    public boolean containsField(final String field) {
        return super.containsKey(field);
    }
    
    @Deprecated
    public boolean containsKey(final String key) {
        return this.containsField(key);
    }
    
    public Object get(final String key) {
        return super.get(key);
    }
    
    public int getInt(final String key) {
        final Object o = this.get(key);
        if (o == null) {
            throw new NullPointerException("no value for: " + key);
        }
        return BSON.toInt(o);
    }
    
    public int getInt(final String key, final int def) {
        final Object foo = this.get(key);
        if (foo == null) {
            return def;
        }
        return BSON.toInt(foo);
    }
    
    public long getLong(final String key) {
        final Object foo = this.get(key);
        return ((Number)foo).longValue();
    }
    
    public long getLong(final String key, final long def) {
        final Object foo = this.get(key);
        if (foo == null) {
            return def;
        }
        return ((Number)foo).longValue();
    }
    
    public double getDouble(final String key) {
        final Object foo = this.get(key);
        return ((Number)foo).doubleValue();
    }
    
    public double getDouble(final String key, final double def) {
        final Object foo = this.get(key);
        if (foo == null) {
            return def;
        }
        return ((Number)foo).doubleValue();
    }
    
    public String getString(final String key) {
        final Object foo = this.get(key);
        if (foo == null) {
            return null;
        }
        return foo.toString();
    }
    
    public String getString(final String key, final String def) {
        final Object foo = this.get(key);
        if (foo == null) {
            return def;
        }
        return foo.toString();
    }
    
    public boolean getBoolean(final String key) {
        return this.getBoolean(key, false);
    }
    
    public boolean getBoolean(final String key, final boolean def) {
        final Object foo = this.get(key);
        if (foo == null) {
            return def;
        }
        if (foo instanceof Number) {
            return ((Number)foo).intValue() > 0;
        }
        if (foo instanceof Boolean) {
            return (boolean)foo;
        }
        throw new IllegalArgumentException("can't coerce to bool:" + foo.getClass());
    }
    
    public ObjectId getObjectId(final String field) {
        return (ObjectId)this.get(field);
    }
    
    public ObjectId getObjectId(final String field, final ObjectId def) {
        final Object foo = this.get(field);
        return (ObjectId)((foo != null) ? foo : def);
    }
    
    public Date getDate(final String field) {
        return (Date)this.get(field);
    }
    
    public Date getDate(final String field, final Date def) {
        final Object foo = this.get(field);
        return (Date)((foo != null) ? foo : def);
    }
    
    public Object put(final String key, final Object val) {
        return super.put(key, val);
    }
    
    public void putAll(final Map m) {
        for (final Map.Entry entry : m.entrySet()) {
            this.put(entry.getKey().toString(), entry.getValue());
        }
    }
    
    public void putAll(final BSONObject o) {
        for (final String k : o.keySet()) {
            this.put(k, o.get(k));
        }
    }
    
    public BasicBSONObject append(final String key, final Object val) {
        this.put(key, val);
        return this;
    }
    
    public String toString() {
        return JSONSerializers.getStrict().serialize(this);
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BSONObject)) {
            return false;
        }
        final BSONObject other = (BSONObject)o;
        return ((HashMap<String, V>)this).keySet().equals(other.keySet()) && Arrays.equals(canonicalizeBSONObject(this).encode(), canonicalizeBSONObject(other).encode());
    }
    
    public int hashCode() {
        return Arrays.hashCode(canonicalizeBSONObject(this).encode());
    }
    
    private byte[] encode() {
        return new BasicBSONEncoder().encode(this);
    }
    
    private BSONObject decode(final byte[] encodedBytes) {
        return new BasicBSONDecoder().readObject(encodedBytes);
    }
    
    private static Object canonicalize(final Object from) {
        if (from instanceof BSONObject && !(from instanceof BasicBSONList)) {
            return canonicalizeBSONObject((BSONObject)from);
        }
        if (from instanceof List) {
            return canonicalizeList((List<Object>)from);
        }
        if (from instanceof Map) {
            return canonicalizeMap((Map<String, Object>)from);
        }
        return from;
    }
    
    private static Map<String, Object> canonicalizeMap(final Map<String, Object> from) {
        final Map<String, Object> canonicalized = new LinkedHashMap<String, Object>(from.size());
        final TreeSet<String> keysInOrder = new TreeSet<String>(from.keySet());
        for (final String key : keysInOrder) {
            final Object val = from.get(key);
            canonicalized.put(key, canonicalize(val));
        }
        return canonicalized;
    }
    
    private static BasicBSONObject canonicalizeBSONObject(final BSONObject from) {
        final BasicBSONObject canonicalized = new BasicBSONObject();
        final TreeSet<String> keysInOrder = new TreeSet<String>(from.keySet());
        for (final String key : keysInOrder) {
            final Object val = from.get(key);
            canonicalized.put(key, canonicalize(val));
        }
        return canonicalized;
    }
    
    private static List canonicalizeList(final List<Object> list) {
        final List<Object> canonicalized = new ArrayList<Object>(list.size());
        for (final Object cur : list) {
            canonicalized.add(canonicalize(cur));
        }
        return canonicalized;
    }
}
