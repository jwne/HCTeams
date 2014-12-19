package com.mongodb.util;

import java.util.*;

@Deprecated
public class Args
{
    final Map<String, String> _options;
    final List<String> _params;
    
    public Args(final String[] args) {
        super();
        this._options = new HashMap<String, String>();
        this._params = new ArrayList<String>();
        for (String s : args) {
            if (s.startsWith("-")) {
                s = s.substring(1);
                final int idx = s.indexOf("=");
                if (idx < 0) {
                    this._options.put(s, "");
                }
                else {
                    this._options.put(s.substring(0, idx), s.substring(idx + 1));
                }
            }
            else {
                this._params.add(s);
            }
        }
    }
    
    public String getOption(final String name) {
        return this._options.get(name);
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        for (final String p : this._options.keySet()) {
            s.append('-').append(p);
            final String v = this._options.get(p);
            if (v.length() == 0) {
                continue;
            }
            s.append('=');
            if (v.indexOf(" ") >= 0) {
                s.append('\"').append(v).append('\"');
            }
            else {
                s.append(v);
            }
        }
        for (final String p : this._params) {
            s.append(' ');
            if (p.indexOf(" ") >= 0) {
                s.append('\"').append(p).append('\"');
            }
            else {
                s.append(p);
            }
        }
        return s.toString();
    }
}
