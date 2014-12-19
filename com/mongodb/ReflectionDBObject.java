package com.mongodb;

import org.bson.*;
import java.util.*;
import java.lang.reflect.*;

public abstract class ReflectionDBObject implements DBObject
{
    JavaWrapper _wrapper;
    Object _id;
    private static final Map<Class, JavaWrapper> _wrappers;
    private static final Set<String> IGNORE_FIELDS;
    
    public Object get(final String key) {
        return this.getWrapper().get(this, key);
    }
    
    public Set<String> keySet() {
        return this.getWrapper().keySet();
    }
    
    @Deprecated
    public boolean containsKey(final String s) {
        return this.containsField(s);
    }
    
    public boolean containsField(final String s) {
        return this.getWrapper().containsKey(s);
    }
    
    public Object put(final String key, final Object v) {
        return this.getWrapper().set(this, key, v);
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
    
    public Object get_id() {
        return this._id;
    }
    
    public void set_id(final Object id) {
        this._id = id;
    }
    
    public boolean isPartialObject() {
        return false;
    }
    
    public Map toMap() {
        final Map m = new HashMap();
        for (final Object s : this.keySet()) {
            m.put(s, this.get(s + ""));
        }
        return m;
    }
    
    public void markAsPartialObject() {
        throw new RuntimeException("ReflectionDBObjects can't be partial");
    }
    
    public Object removeField(final String key) {
        throw new RuntimeException("can't remove from a ReflectionDBObject");
    }
    
    JavaWrapper getWrapper() {
        if (this._wrapper != null) {
            return this._wrapper;
        }
        return this._wrapper = getWrapper(this.getClass());
    }
    
    public static JavaWrapper getWrapperIfReflectionObject(final Class c) {
        if (ReflectionDBObject.class.isAssignableFrom(c)) {
            return getWrapper(c);
        }
        return null;
    }
    
    public static JavaWrapper getWrapper(final Class c) {
        JavaWrapper w = ReflectionDBObject._wrappers.get(c);
        if (w == null) {
            w = new JavaWrapper(c);
            ReflectionDBObject._wrappers.put(c, w);
        }
        return w;
    }
    
    static {
        _wrappers = Collections.synchronizedMap(new HashMap<Class, JavaWrapper>());
        (IGNORE_FIELDS = new HashSet<String>()).add("Int");
    }
    
    public static class JavaWrapper
    {
        final Class _class;
        final String _name;
        final Map<String, FieldInfo> _fields;
        final Set<String> _keys;
        
        JavaWrapper(final Class c) {
            super();
            this._class = c;
            this._name = c.getName();
            this._fields = new TreeMap<String, FieldInfo>();
            for (final Method m : c.getMethods()) {
                if (m.getName().startsWith("get") || m.getName().startsWith("set")) {
                    final String name = m.getName().substring(3);
                    if (name.length() != 0) {
                        if (!ReflectionDBObject.IGNORE_FIELDS.contains(name)) {
                            final Class type = m.getName().startsWith("get") ? m.getReturnType() : m.getParameterTypes()[0];
                            FieldInfo fi = this._fields.get(name);
                            if (fi == null) {
                                fi = new FieldInfo(name, type);
                                this._fields.put(name, fi);
                            }
                            if (m.getName().startsWith("get")) {
                                fi._getter = m;
                            }
                            else {
                                fi._setter = m;
                            }
                        }
                    }
                }
            }
            final Set<String> names = new HashSet<String>(this._fields.keySet());
            for (final String name2 : names) {
                if (!this._fields.get(name2).ok()) {
                    this._fields.remove(name2);
                }
            }
            this._keys = Collections.unmodifiableSet((Set<? extends String>)this._fields.keySet());
        }
        
        public Set<String> keySet() {
            return this._keys;
        }
        
        @Deprecated
        public boolean containsKey(final String key) {
            return this._keys.contains(key);
        }
        
        public Object get(final ReflectionDBObject document, final String fieldName) {
            final FieldInfo i = this._fields.get(fieldName);
            if (i == null) {
                return null;
            }
            try {
                return i._getter.invoke(document, new Object[0]);
            }
            catch (Exception e) {
                throw new RuntimeException("could not invoke getter for [" + fieldName + "] on [" + this._name + "]", e);
            }
        }
        
        public Object set(final ReflectionDBObject t, final String name, final Object val) {
            final FieldInfo i = this._fields.get(name);
            if (i == null) {
                throw new IllegalArgumentException("no field [" + name + "] on [" + this._name + "]");
            }
            try {
                return i._setter.invoke(t, val);
            }
            catch (Exception e) {
                throw new RuntimeException("could not invoke setter for [" + name + "] on [" + this._name + "]", e);
            }
        }
        
        Class getInternalClass(final String path) {
            String cur = path;
            String next = null;
            final int idx = path.indexOf(".");
            if (idx >= 0) {
                cur = path.substring(0, idx);
                next = path.substring(idx + 1);
            }
            final FieldInfo fi = this._fields.get(cur);
            if (fi == null) {
                return null;
            }
            if (next == null) {
                return fi._class;
            }
            final JavaWrapper w = ReflectionDBObject.getWrapperIfReflectionObject(fi._class);
            if (w == null) {
                return null;
            }
            return w.getInternalClass(next);
        }
    }
    
    static class FieldInfo
    {
        final String _name;
        final Class _class;
        Method _getter;
        Method _setter;
        
        FieldInfo(final String name, final Class c) {
            super();
            this._name = name;
            this._class = c;
        }
        
        boolean ok() {
            return this._getter != null && this._setter != null;
        }
    }
}
