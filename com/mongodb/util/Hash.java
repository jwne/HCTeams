package com.mongodb.util;

@Deprecated
public final class Hash
{
    static final long _longHashConstant = 4095L;
    
    public static final int hashBackward(final String s) {
        int hash = 0;
        for (int i = s.length() - 1; i >= 0; --i) {
            hash = hash * 31 + s.charAt(i);
        }
        return hash;
    }
    
    public static final long hashBackwardLong(final String s) {
        long hash = 0L;
        for (int i = s.length() - 1; i >= 0; --i) {
            hash = hash * 63L + s.charAt(i);
        }
        return hash;
    }
    
    public static final long longHash(final String s) {
        return longHash(s, 0, s.length());
    }
    
    public static final long longHash(final String s, int start, final int end) {
        long hash = 0L;
        while (start < end) {
            hash = 4095L * hash + s.charAt(start);
            ++start;
        }
        return hash;
    }
    
    public static final long longLowerHash(final String s) {
        return longLowerHash(s, 0, s.length());
    }
    
    public static final long longLowerHash(final String s, int start, final int end) {
        long hash = 0L;
        while (start < end) {
            hash = 4095L * hash + Character.toLowerCase(s.charAt(start));
            ++start;
        }
        return hash;
    }
    
    public static final long longLowerHash(final String s, int start, final int end, long hash) {
        while (start < end) {
            hash = 4095L * hash + Character.toLowerCase(s.charAt(start));
            ++start;
        }
        return hash;
    }
    
    public static final long longLowerHashAppend(final long hash, final char c) {
        return hash * 4095L + Character.toLowerCase(c);
    }
    
    public static final long longHashAppend(final long hash, final char c) {
        return hash * 4095L + c;
    }
    
    public static final int lowerCaseHash(final String s) {
        int h = 0;
        for (int len = s.length(), i = 0; i < len; ++i) {
            h = 31 * h + Character.toLowerCase(s.charAt(i));
        }
        return h;
    }
    
    public static final int lowerCaseHash(final String s, final int start, final int end) {
        int h = 0;
        for (int len = s.length(), i = start; i < len && i < end; ++i) {
            h = 31 * h + Character.toLowerCase(s.charAt(i));
        }
        return h;
    }
    
    public static final int hashCode(final CharSequence s, final int start, final int end) {
        int h = 0;
        for (int len = s.length(), i = start; i < len && i < end; ++i) {
            h = 31 * h + s.charAt(i);
        }
        return h;
    }
    
    public static final int nospaceLowerHash(final String s, final int start, final int end) {
        int h = 0;
        for (int len = s.length(), i = start; i < len && i < end; ++i) {
            final char c = s.charAt(i);
            if (!Character.isWhitespace(c)) {
                h = 31 * h + Character.toLowerCase(c);
            }
        }
        return h;
    }
    
    public static final int lowerCaseSpaceTrimHash(final String s) {
        int h = 0;
        int len;
        for (len = s.length(); len > 1 && Character.isWhitespace(s.charAt(len - 1)); --len) {}
        boolean lastWasSpace = true;
        for (int i = 0; i < len; ++i) {
            final boolean isSpace = Character.isWhitespace(s.charAt(i));
            if (!isSpace || !lastWasSpace) {
                lastWasSpace = isSpace;
                h = 31 * h + Character.toLowerCase(s.charAt(i));
            }
        }
        return h;
    }
    
    public static final int lowerCaseSpaceTrimHash(final String s, final int start, final int end) {
        int h = 0;
        int len;
        for (len = s.length(); len > 1 && Character.isWhitespace(s.charAt(len - 1)); --len) {}
        boolean lastWasSpace = true;
        for (int i = start; i < len && i < end; ++i) {
            final boolean isSpace = Character.isWhitespace(s.charAt(i));
            if (!isSpace || !lastWasSpace) {
                lastWasSpace = isSpace;
                h = 31 * h + Character.toLowerCase(s.charAt(i));
            }
        }
        return h;
    }
    
    public static final int hashCode(final String... strings) {
        int h = 0;
        for (final String s : strings) {
            for (int len = s.length(), i = 0; i < len; ++i) {
                h = 31 * h + s.charAt(i);
            }
        }
        return h;
    }
}
