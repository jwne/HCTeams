package com.mongodb;

import com.mongodb.util.*;
import java.io.*;

class NativeAuthenticationHelper
{
    static DBObject getAuthCommand(final String userName, final char[] password, final String nonce) {
        return getAuthCommand(userName, createHash(userName, password), nonce);
    }
    
    static DBObject getAuthCommand(final String userName, final byte[] authHash, final String nonce) {
        final String key = nonce + userName + new String(authHash);
        final BasicDBObject cmd = new BasicDBObject();
        cmd.put("authenticate", 1);
        cmd.put("user", userName);
        cmd.put("nonce", nonce);
        cmd.put("key", Util.hexMD5(key.getBytes()));
        return cmd;
    }
    
    static BasicDBObject getNonceCommand() {
        return new BasicDBObject("getnonce", 1);
    }
    
    static byte[] createHash(final String userName, final char[] password) {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream(userName.length() + 20 + password.length);
        try {
            bout.write(userName.getBytes());
            bout.write(":mongo:".getBytes());
            for (final char ch : password) {
                if (ch >= '\u0080') {
                    throw new IllegalArgumentException("can't handle non-ascii passwords yet");
                }
                bout.write((byte)ch);
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException("impossible", ioe);
        }
        return Util.hexMD5(bout.toByteArray()).getBytes();
    }
}
