package org.bson;

import java.util.*;
import org.bson.io.*;

@Deprecated
public class KeyCachingLazyBSONObject extends LazyBSONObject
{
    private HashMap<String, ElementRecord> fieldIndex;
    
    public KeyCachingLazyBSONObject(final byte[] data, final LazyBSONCallback cbk) {
        super(data, cbk);
        this.fieldIndex = new HashMap<String, ElementRecord>();
    }
    
    public KeyCachingLazyBSONObject(final byte[] data, final int offset, final LazyBSONCallback cbk) {
        super(data, offset, cbk);
        this.fieldIndex = new HashMap<String, ElementRecord>();
    }
    
    public KeyCachingLazyBSONObject(final BSONByteBuffer buffer, final LazyBSONCallback callback) {
        super(buffer, callback);
        this.fieldIndex = new HashMap<String, ElementRecord>();
    }
    
    public KeyCachingLazyBSONObject(final BSONByteBuffer buffer, final int offset, final LazyBSONCallback callback) {
        super(buffer, offset, callback);
        this.fieldIndex = new HashMap<String, ElementRecord>();
    }
    
    public Object get(final String key) {
        this.ensureFieldList();
        return super.get(key);
    }
    
    public boolean containsField(final String s) {
        this.ensureFieldList();
        return this.fieldIndex.containsKey(s) && super.containsField(s);
    }
    
    private synchronized void ensureFieldList() {
        if (this.fieldIndex == null) {
            return;
        }
        try {
            int fieldSize;
            int elementSize;
            for (int offset = this._doc_start_offset + 4; !this.isElementEmpty(offset); offset += fieldSize + elementSize) {
                fieldSize = this.sizeCString(offset);
                elementSize = this.getElementBSONSize(offset++);
                final String name = this._input.getCString(offset);
                final ElementRecord _t_record = new ElementRecord(this, name, offset);
                this.fieldIndex.put(name, _t_record);
            }
        }
        catch (Exception e) {
            this.fieldIndex = new HashMap<String, ElementRecord>();
        }
    }
}
