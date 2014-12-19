package com.mongodb.util;

import java.util.*;

@Deprecated
public class OptionMap extends TreeMap<String, String>
{
    private static final long serialVersionUID = -4415279469780082174L;
    
    public int getInt(final String name, final int def) {
        return StringParseUtil.parseIfInt(((TreeMap<K, String>)this).get(name), def);
    }
}
