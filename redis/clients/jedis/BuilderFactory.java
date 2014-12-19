package redis.clients.jedis;

import redis.clients.util.*;
import java.util.*;

public class BuilderFactory
{
    public static final Builder<Double> DOUBLE;
    public static final Builder<Boolean> BOOLEAN;
    public static final Builder<byte[]> BYTE_ARRAY;
    public static final Builder<Long> LONG;
    public static final Builder<String> STRING;
    public static final Builder<List<String>> STRING_LIST;
    public static final Builder<Map<String, String>> STRING_MAP;
    public static final Builder<Map<String, String>> PUBSUB_NUMSUB_MAP;
    public static final Builder<Set<String>> STRING_SET;
    public static final Builder<List<byte[]>> BYTE_ARRAY_LIST;
    public static final Builder<Set<byte[]>> BYTE_ARRAY_ZSET;
    public static final Builder<Map<byte[], byte[]>> BYTE_ARRAY_MAP;
    public static final Builder<Set<String>> STRING_ZSET;
    public static final Builder<Set<Tuple>> TUPLE_ZSET;
    public static final Builder<Set<Tuple>> TUPLE_ZSET_BINARY;
    
    static {
        DOUBLE = new Builder<Double>() {
            @Override
            public Double build(final Object data) {
                final String asString = BuilderFactory.STRING.build(data);
                return (asString == null) ? null : Double.valueOf(asString);
            }
            
            @Override
            public String toString() {
                return "double";
            }
        };
        BOOLEAN = new Builder<Boolean>() {
            @Override
            public Boolean build(final Object data) {
                return (long)data == 1L;
            }
            
            @Override
            public String toString() {
                return "boolean";
            }
        };
        BYTE_ARRAY = new Builder<byte[]>() {
            @Override
            public byte[] build(final Object data) {
                return (byte[])data;
            }
            
            @Override
            public String toString() {
                return "byte[]";
            }
        };
        LONG = new Builder<Long>() {
            @Override
            public Long build(final Object data) {
                return (Long)data;
            }
            
            @Override
            public String toString() {
                return "long";
            }
        };
        STRING = new Builder<String>() {
            @Override
            public String build(final Object data) {
                return (data == null) ? null : SafeEncoder.encode((byte[])data);
            }
            
            @Override
            public String toString() {
                return "string";
            }
        };
        STRING_LIST = new Builder<List<String>>() {
            @Override
            public List<String> build(final Object data) {
                if (null == data) {
                    return null;
                }
                final List<byte[]> l = (List<byte[]>)data;
                final ArrayList<String> result = new ArrayList<String>(l.size());
                for (final byte[] barray : l) {
                    if (barray == null) {
                        result.add(null);
                    }
                    else {
                        result.add(SafeEncoder.encode(barray));
                    }
                }
                return result;
            }
            
            @Override
            public String toString() {
                return "List<String>";
            }
        };
        STRING_MAP = new Builder<Map<String, String>>() {
            @Override
            public Map<String, String> build(final Object data) {
                final List<byte[]> flatHash = (List<byte[]>)data;
                final Map<String, String> hash = new HashMap<String, String>();
                final Iterator<byte[]> iterator = flatHash.iterator();
                while (iterator.hasNext()) {
                    hash.put(SafeEncoder.encode(iterator.next()), SafeEncoder.encode(iterator.next()));
                }
                return hash;
            }
            
            @Override
            public String toString() {
                return "Map<String, String>";
            }
        };
        PUBSUB_NUMSUB_MAP = new Builder<Map<String, String>>() {
            @Override
            public Map<String, String> build(final Object data) {
                final List<Object> flatHash = (List<Object>)data;
                final Map<String, String> hash = new HashMap<String, String>();
                final Iterator<Object> iterator = flatHash.iterator();
                while (iterator.hasNext()) {
                    hash.put(SafeEncoder.encode(iterator.next()), String.valueOf(iterator.next()));
                }
                return hash;
            }
            
            @Override
            public String toString() {
                return "PUBSUB_NUMSUB_MAP<String, String>";
            }
        };
        STRING_SET = new Builder<Set<String>>() {
            @Override
            public Set<String> build(final Object data) {
                if (null == data) {
                    return null;
                }
                final List<byte[]> l = (List<byte[]>)data;
                final Set<String> result = new HashSet<String>(l.size());
                for (final byte[] barray : l) {
                    if (barray == null) {
                        result.add(null);
                    }
                    else {
                        result.add(SafeEncoder.encode(barray));
                    }
                }
                return result;
            }
            
            @Override
            public String toString() {
                return "Set<String>";
            }
        };
        BYTE_ARRAY_LIST = new Builder<List<byte[]>>() {
            @Override
            public List<byte[]> build(final Object data) {
                if (null == data) {
                    return null;
                }
                final List<byte[]> l = (List<byte[]>)data;
                return l;
            }
            
            @Override
            public String toString() {
                return "List<byte[]>";
            }
        };
        BYTE_ARRAY_ZSET = new Builder<Set<byte[]>>() {
            @Override
            public Set<byte[]> build(final Object data) {
                if (null == data) {
                    return null;
                }
                final List<byte[]> l = (List<byte[]>)data;
                final Set<byte[]> result = new LinkedHashSet<byte[]>(l);
                for (final byte[] barray : l) {
                    if (barray == null) {
                        result.add(null);
                    }
                    else {
                        result.add(barray);
                    }
                }
                return result;
            }
            
            @Override
            public String toString() {
                return "ZSet<byte[]>";
            }
        };
        BYTE_ARRAY_MAP = new Builder<Map<byte[], byte[]>>() {
            @Override
            public Map<byte[], byte[]> build(final Object data) {
                final List<byte[]> flatHash = (List<byte[]>)data;
                final Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
                final Iterator<byte[]> iterator = flatHash.iterator();
                while (iterator.hasNext()) {
                    hash.put(iterator.next(), iterator.next());
                }
                return hash;
            }
            
            @Override
            public String toString() {
                return "Map<byte[], byte[]>";
            }
        };
        STRING_ZSET = new Builder<Set<String>>() {
            @Override
            public Set<String> build(final Object data) {
                if (null == data) {
                    return null;
                }
                final List<byte[]> l = (List<byte[]>)data;
                final Set<String> result = new LinkedHashSet<String>(l.size());
                for (final byte[] barray : l) {
                    if (barray == null) {
                        result.add(null);
                    }
                    else {
                        result.add(SafeEncoder.encode(barray));
                    }
                }
                return result;
            }
            
            @Override
            public String toString() {
                return "ZSet<String>";
            }
        };
        TUPLE_ZSET = new Builder<Set<Tuple>>() {
            @Override
            public Set<Tuple> build(final Object data) {
                if (null == data) {
                    return null;
                }
                final List<byte[]> l = (List<byte[]>)data;
                final Set<Tuple> result = new LinkedHashSet<Tuple>(l.size());
                final Iterator<byte[]> iterator = l.iterator();
                while (iterator.hasNext()) {
                    result.add(new Tuple(SafeEncoder.encode(iterator.next()), Double.valueOf(SafeEncoder.encode(iterator.next()))));
                }
                return result;
            }
            
            @Override
            public String toString() {
                return "ZSet<Tuple>";
            }
        };
        TUPLE_ZSET_BINARY = new Builder<Set<Tuple>>() {
            @Override
            public Set<Tuple> build(final Object data) {
                if (null == data) {
                    return null;
                }
                final List<byte[]> l = (List<byte[]>)data;
                final Set<Tuple> result = new LinkedHashSet<Tuple>(l.size());
                final Iterator<byte[]> iterator = l.iterator();
                while (iterator.hasNext()) {
                    result.add(new Tuple(iterator.next(), Double.valueOf(SafeEncoder.encode(iterator.next()))));
                }
                return result;
            }
            
            @Override
            public String toString() {
                return "ZSet<Tuple>";
            }
        };
    }
}
