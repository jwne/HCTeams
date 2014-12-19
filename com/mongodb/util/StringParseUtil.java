package com.mongodb.util;

@Deprecated
public final class StringParseUtil
{
    public static boolean parseBoolean(String s, final boolean d) {
        if (s == null) {
            return d;
        }
        s = s.trim();
        if (s.length() == 0) {
            return d;
        }
        final char c = s.charAt(0);
        return c == 't' || c == 'T' || c == 'y' || c == 'Y' || (c != 'f' && c != 'F' && c != 'n' && c != 'N' && d);
    }
    
    public static int parseInt(final String s, final int def) {
        return parseInt(s, def, null, true);
    }
    
    public static Number parseIntRadix(String s, final int radix) {
        if (s == null) {
            return Double.NaN;
        }
        s = s.trim();
        if (s.length() == 0) {
            return Double.NaN;
        }
        final int firstDigit = -1;
        int i = 0;
        if (s.charAt(0) == '-') {
            i = 1;
        }
        while (i < s.length()) {
            if (Character.digit(s.charAt(i), radix) == -1) {
                break;
            }
            ++i;
        }
        try {
            return Long.valueOf(s.substring(0, i), radix);
        }
        catch (Exception e) {
            return Double.NaN;
        }
    }
    
    public static int parseInt(String s, final int def, final int[] lastIdx, final boolean allowNegative) {
        final boolean useLastIdx = lastIdx != null && lastIdx.length > 0;
        if (useLastIdx) {
            lastIdx[0] = -1;
        }
        if (s == null) {
            return def;
        }
        s = s.trim();
        if (s.length() == 0) {
            return def;
        }
        int firstDigit = -1;
        for (int i = 0; i < s.length(); ++i) {
            if (Character.isDigit(s.charAt(i))) {
                firstDigit = i;
                break;
            }
        }
        if (firstDigit < 0) {
            return def;
        }
        int lastDigit;
        for (lastDigit = firstDigit + 1; lastDigit < s.length() && Character.isDigit(s.charAt(lastDigit)); ++lastDigit) {}
        if (allowNegative && firstDigit > 0 && s.charAt(firstDigit - 1) == '-') {
            --firstDigit;
        }
        if (useLastIdx) {
            lastIdx[0] = lastDigit;
        }
        return Integer.parseInt(s.substring(firstDigit, lastDigit));
    }
    
    public static Number parseNumber(String s, final Number def) {
        if (s == null) {
            return def;
        }
        s = s.trim();
        if (s.length() == 0) {
            return def;
        }
        int firstDigit = -1;
        for (int i = 0; i < s.length(); ++i) {
            if (Character.isDigit(s.charAt(i))) {
                firstDigit = i;
                break;
            }
        }
        if (firstDigit < 0) {
            return def;
        }
        int lastDigit;
        for (lastDigit = firstDigit + 1; lastDigit < s.length() && Character.isDigit(s.charAt(lastDigit)); ++lastDigit) {}
        boolean isDouble = false;
        if (firstDigit > 0 && s.charAt(firstDigit - 1) == '.') {
            --firstDigit;
            isDouble = true;
        }
        if (firstDigit > 0 && s.charAt(firstDigit - 1) == '-') {
            --firstDigit;
        }
        if (lastDigit < s.length() && s.charAt(lastDigit) == '.') {
            ++lastDigit;
            while (lastDigit < s.length() && Character.isDigit(s.charAt(lastDigit))) {
                ++lastDigit;
            }
            isDouble = true;
        }
        if (lastDigit < s.length() && s.charAt(lastDigit) == 'E') {
            ++lastDigit;
            while (lastDigit < s.length() && Character.isDigit(s.charAt(lastDigit))) {
                ++lastDigit;
            }
            isDouble = true;
        }
        final String actual = s.substring(firstDigit, lastDigit);
        if (isDouble || actual.length() > 17) {
            return Double.valueOf(actual);
        }
        if (actual.length() > 10) {
            return Long.valueOf(actual);
        }
        return Integer.valueOf(actual);
    }
    
    public static Number parseStrict(String s) {
        if (s.length() == 0) {
            return 0;
        }
        if (s.charAt(0) == '+') {
            s = s.substring(1);
        }
        if (s.matches("(\\+|-)?Infinity")) {
            if (s.startsWith("-")) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        else {
            if (s.indexOf(46) != -1 || s.equals("-0")) {
                return Double.valueOf(s);
            }
            if (s.toLowerCase().indexOf("0x") > -1) {
                final int coef = (s.charAt(0) == '-') ? -1 : 1;
                if (s.length() > 17) {
                    throw new RuntimeException("Can't handle a number this big: " + s);
                }
                if (s.length() > 9) {
                    return coef * Long.valueOf(s.substring((int)(coef * -0.5 + 2.5)), 16);
                }
                return coef * Integer.valueOf(s.substring((int)(coef * -0.5 + 2.5)), 16);
            }
            else {
                final int e = s.toLowerCase().indexOf(101);
                if (e > 0) {
                    final double num = Double.parseDouble(s.substring(0, e));
                    final int exp = Integer.parseInt(s.substring(e + 1));
                    return num * Math.pow(10.0, exp);
                }
                if (s.length() > 17) {
                    return Double.valueOf(s);
                }
                if (s.length() > 9) {
                    return Long.valueOf(s);
                }
                return Integer.valueOf(s);
            }
        }
    }
    
    public static int parseIfInt(String s, final int def) {
        if (s == null || s.length() == 0) {
            return def;
        }
        s = s.trim();
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isDigit(s.charAt(i))) {
                return def;
            }
        }
        return Integer.parseInt(s);
    }
}
