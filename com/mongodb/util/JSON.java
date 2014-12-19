package com.mongodb.util;

import org.bson.*;
import com.mongodb.*;

public class JSON
{
    public static String serialize(final Object object) {
        final StringBuilder buf = new StringBuilder();
        serialize(object, buf);
        return buf.toString();
    }
    
    public static void serialize(final Object object, final StringBuilder buf) {
        JSONSerializers.getLegacy().serialize(object, buf);
    }
    
    public static Object parse(final String jsonString) {
        return parse(jsonString, null);
    }
    
    public static Object parse(String s, final BSONCallback c) {
        if (s == null || (s = s.trim()).equals("")) {
            return null;
        }
        final JSONParser p = new JSONParser(s, c);
        return p.parse();
    }
    
    static void string(final StringBuilder a, final String s) {
        a.append("\"");
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (c == '\\') {
                a.append("\\\\");
            }
            else if (c == '\"') {
                a.append("\\\"");
            }
            else if (c == '\n') {
                a.append("\\n");
            }
            else if (c == '\r') {
                a.append("\\r");
            }
            else if (c == '\t') {
                a.append("\\t");
            }
            else if (c == '\b') {
                a.append("\\b");
            }
            else if (c >= ' ') {
                a.append(c);
            }
        }
        a.append("\"");
    }
}
