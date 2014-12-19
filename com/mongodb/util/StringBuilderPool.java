package com.mongodb.util;

import java.io.*;

@Deprecated
public class StringBuilderPool extends SimplePool<StringBuilder>
{
    public StringBuilderPool(final String name, final int maxToKeep) {
        super("StringBuilderPool-" + name, maxToKeep);
    }
    
    public StringBuilder createNew() {
        return new StringBuilder();
    }
    
    public boolean ok(final StringBuilder buf) {
        if (buf.length() > this.getMaxSize()) {
            return false;
        }
        buf.setLength(0);
        return true;
    }
    
    protected long memSize(final StringBuilder buf) {
        return buf.length() * 2;
    }
}
