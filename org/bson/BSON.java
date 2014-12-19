package org.bson;

import java.util.logging.*;
import org.bson.util.*;
import java.nio.charset.*;
import java.util.concurrent.*;
import java.util.*;

public class BSON
{
    static final Logger LOGGER;
    public static final byte EOO = 0;
    public static final byte NUMBER = 1;
    public static final byte STRING = 2;
    public static final byte OBJECT = 3;
    public static final byte ARRAY = 4;
    public static final byte BINARY = 5;
    public static final byte UNDEFINED = 6;
    public static final byte OID = 7;
    public static final byte BOOLEAN = 8;
    public static final byte DATE = 9;
    public static final byte NULL = 10;
    public static final byte REGEX = 11;
    public static final byte REF = 12;
    public static final byte CODE = 13;
    public static final byte SYMBOL = 14;
    public static final byte CODE_W_SCOPE = 15;
    public static final byte NUMBER_INT = 16;
    public static final byte TIMESTAMP = 17;
    public static final byte NUMBER_LONG = 18;
    public static final byte MINKEY = -1;
    public static final byte MAXKEY = Byte.MAX_VALUE;
    public static final byte B_GENERAL = 0;
    public static final byte B_FUNC = 1;
    public static final byte B_BINARY = 2;
    public static final byte B_UUID = 3;
    private static final int GLOBAL_FLAG = 256;
    private static boolean _encodeHooks;
    private static boolean _decodeHooks;
    static ClassMap<List<Transformer>> _encodingHooks;
    static ClassMap<List<Transformer>> _decodingHooks;
    @Deprecated
    protected static Charset _utf8;
    static ThreadLocal<BSONEncoder> _staticEncoder;
    static ThreadLocal<BSONDecoder> _staticDecoder;
    
    public static int regexFlags(String flags) {
        int fint = 0;
        if (flags == null || flags.length() == 0) {
            return fint;
        }
        flags = flags.toLowerCase();
        for (int i = 0; i < flags.length(); ++i) {
            final RegexFlag flag = RegexFlag.getByCharacter(flags.charAt(i));
            if (flag == null) {
                throw new IllegalArgumentException("unrecognized flag [" + flags.charAt(i) + "] " + (int)flags.charAt(i));
            }
            fint |= flag.javaFlag;
            if (flag.unsupported != null) {
                _warnUnsupportedRegex(flag.unsupported);
            }
        }
        return fint;
    }
    
    public static int regexFlag(final char c) {
        final RegexFlag flag = RegexFlag.getByCharacter(c);
        if (flag == null) {
            throw new IllegalArgumentException("unrecognized flag [" + c + "]");
        }
        if (flag.unsupported != null) {
            _warnUnsupportedRegex(flag.unsupported);
            return 0;
        }
        return flag.javaFlag;
    }
    
    public static String regexFlags(int flags) {
        final StringBuilder buf = new StringBuilder();
        for (final RegexFlag flag : RegexFlag.values()) {
            if ((flags & flag.javaFlag) > 0) {
                buf.append(flag.flagChar);
                flags -= flag.javaFlag;
            }
        }
        if (flags > 0) {
            throw new IllegalArgumentException("some flags could not be recognized.");
        }
        return buf.toString();
    }
    
    private static void _warnUnsupportedRegex(final String flag) {
        BSON.LOGGER.info("flag " + flag + " not supported by db.");
    }
    
    public static boolean hasDecodeHooks() {
        return BSON._decodeHooks;
    }
    
    public static void addEncodingHook(final Class c, final Transformer t) {
        BSON._encodeHooks = true;
        List<Transformer> l = BSON._encodingHooks.get(c);
        if (l == null) {
            l = new CopyOnWriteArrayList<Transformer>();
            BSON._encodingHooks.put(c, l);
        }
        l.add(t);
    }
    
    public static void addDecodingHook(final Class c, final Transformer t) {
        BSON._decodeHooks = true;
        List<Transformer> l = BSON._decodingHooks.get(c);
        if (l == null) {
            l = new CopyOnWriteArrayList<Transformer>();
            BSON._decodingHooks.put(c, l);
        }
        l.add(t);
    }
    
    public static Object applyEncodingHooks(Object o) {
        if (!_anyHooks()) {
            return o;
        }
        if (BSON._encodingHooks.size() == 0 || o == null) {
            return o;
        }
        final List<Transformer> l = BSON._encodingHooks.get(o.getClass());
        if (l != null) {
            for (final Transformer t : l) {
                o = t.transform(o);
            }
        }
        return o;
    }
    
    public static Object applyDecodingHooks(Object o) {
        if (!_anyHooks() || o == null) {
            return o;
        }
        final List<Transformer> l = BSON._decodingHooks.get(o.getClass());
        if (l != null) {
            for (final Transformer t : l) {
                o = t.transform(o);
            }
        }
        return o;
    }
    
    public static List<Transformer> getEncodingHooks(final Class<?> clazz) {
        return BSON._encodingHooks.get(clazz);
    }
    
    public static void clearEncodingHooks() {
        BSON._encodeHooks = false;
        BSON._encodingHooks.clear();
    }
    
    public static void removeEncodingHooks(final Class c) {
        BSON._encodingHooks.remove(c);
    }
    
    public static void removeEncodingHook(final Class c, final Transformer t) {
        getEncodingHooks(c).remove(t);
    }
    
    public static List<Transformer> getDecodingHooks(final Class clazz) {
        return BSON._decodingHooks.get(clazz);
    }
    
    public static void clearDecodingHooks() {
        BSON._decodeHooks = false;
        BSON._decodingHooks.clear();
    }
    
    public static void removeDecodingHooks(final Class clazz) {
        BSON._decodingHooks.remove(clazz);
    }
    
    public static void removeDecodingHook(final Class c, final Transformer t) {
        getDecodingHooks(c).remove(t);
    }
    
    public static void clearAllHooks() {
        clearEncodingHooks();
        clearDecodingHooks();
    }
    
    private static boolean _anyHooks() {
        return BSON._encodeHooks || BSON._decodeHooks;
    }
    
    public static byte[] encode(final BSONObject o) {
        final BSONEncoder e = BSON._staticEncoder.get();
        try {
            return e.encode(o);
        }
        finally {
            e.done();
        }
    }
    
    public static BSONObject decode(final byte[] b) {
        final BSONDecoder d = BSON._staticDecoder.get();
        return d.readObject(b);
    }
    
    public static int toInt(final Object o) {
        if (o == null) {
            throw new NullPointerException("can't be null");
        }
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        if (o instanceof Boolean) {
            return ((boolean)o) ? 1 : 0;
        }
        throw new IllegalArgumentException("can't convert: " + o.getClass().getName() + " to int");
    }
    
    static {
        LOGGER = Logger.getLogger("org.bson.BSON");
        BSON._encodeHooks = false;
        BSON._decodeHooks = false;
        BSON._encodingHooks = new ClassMap<List<Transformer>>();
        BSON._decodingHooks = new ClassMap<List<Transformer>>();
        BSON._utf8 = Charset.forName("UTF-8");
        BSON._staticEncoder = new ThreadLocal<BSONEncoder>() {
            protected BSONEncoder initialValue() {
                return new BasicBSONEncoder();
            }
        };
        BSON._staticDecoder = new ThreadLocal<BSONDecoder>() {
            protected BSONDecoder initialValue() {
                return new BasicBSONDecoder();
            }
        };
    }
    
    private enum RegexFlag
    {
        CANON_EQ(128, 'c', "Pattern.CANON_EQ"), 
        UNIX_LINES(1, 'd', "Pattern.UNIX_LINES"), 
        GLOBAL(256, 'g', (String)null), 
        CASE_INSENSITIVE(2, 'i', (String)null), 
        MULTILINE(8, 'm', (String)null), 
        DOTALL(32, 's', "Pattern.DOTALL"), 
        LITERAL(16, 't', "Pattern.LITERAL"), 
        UNICODE_CASE(64, 'u', "Pattern.UNICODE_CASE"), 
        COMMENTS(4, 'x', (String)null);
        
        private static final Map<Character, RegexFlag> byCharacter;
        public final int javaFlag;
        public final char flagChar;
        public final String unsupported;
        
        public static RegexFlag getByCharacter(final char ch) {
            return RegexFlag.byCharacter.get(ch);
        }
        
        private RegexFlag(final int f, final char ch, final String u) {
            this.javaFlag = f;
            this.flagChar = ch;
            this.unsupported = u;
        }
        
        static {
            byCharacter = new HashMap<Character, RegexFlag>();
            for (final RegexFlag flag : values()) {
                RegexFlag.byCharacter.put(flag.flagChar, flag);
            }
        }
    }
}
