package org.bson;

import org.bson.io.*;
import java.util.logging.*;
import java.io.*;
import org.bson.types.*;
import java.util.regex.*;
import com.mongodb.util.*;
import java.lang.reflect.*;
import java.util.*;

public class LazyBSONObject implements BSONObject
{
    static final int FIRST_ELMT_OFFSET = 4;
    @Deprecated
    protected final int _doc_start_offset;
    @Deprecated
    protected final BSONByteBuffer _input;
    @Deprecated
    protected final LazyBSONCallback _callback;
    private static final Logger log;
    
    public LazyBSONObject(final byte[] bytes, final LazyBSONCallback callback) {
        this(BSONByteBuffer.wrap(bytes), callback);
    }
    
    public LazyBSONObject(final byte[] bytes, final int offset, final LazyBSONCallback callback) {
        this(BSONByteBuffer.wrap(bytes, offset, bytes.length - offset), offset, callback);
    }
    
    public LazyBSONObject(final BSONByteBuffer buffer, final LazyBSONCallback callback) {
        this(buffer, 0, callback);
    }
    
    public LazyBSONObject(final BSONByteBuffer buffer, final int offset, final LazyBSONCallback callback) {
        super();
        this._callback = callback;
        this._input = buffer;
        this._doc_start_offset = offset;
    }
    
    public Object put(final String key, final Object v) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    public void putAll(final BSONObject o) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    public void putAll(final Map m) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    public Object get(final String key) {
        final ElementRecord element = this.getElement(key);
        if (element == null) {
            return null;
        }
        return this.getElementValue(element);
    }
    
    ElementRecord getElement(final String key) {
        int fieldSize;
        int elementSize;
        for (int offset = this._doc_start_offset + 4; !this.isElementEmpty(offset); offset += fieldSize + elementSize) {
            fieldSize = this.sizeCString(offset + 1);
            elementSize = this.getElementBSONSize(offset);
            final String name = this._input.getCString(++offset);
            if (name.equals(key)) {
                return new ElementRecord(name, offset);
            }
        }
        return null;
    }
    
    List<ElementRecord> getElements() {
        int offset = this._doc_start_offset + 4;
        final ArrayList<ElementRecord> elements = new ArrayList<ElementRecord>();
        while (!this.isElementEmpty(offset)) {
            final int fieldSize = this.sizeCString(offset + 1);
            final int elementSize = this.getElementBSONSize(offset);
            final String name = this._input.getCString(++offset);
            final ElementRecord rec = new ElementRecord(name, offset);
            elements.add(rec);
            offset += fieldSize + elementSize;
        }
        return elements;
    }
    
    public Map toMap() {
        throw new UnsupportedOperationException("Not Supported");
    }
    
    public Object removeField(final String key) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    @Deprecated
    public boolean containsKey(final String s) {
        return this.containsField(s);
    }
    
    public boolean containsField(final String s) {
        return this.keySet().contains(s);
    }
    
    public Set<String> keySet() {
        return new LazyBSONKeySet();
    }
    
    public Set<Map.Entry<String, Object>> entrySet() {
        return new LazyBSONEntrySet();
    }
    
    @Deprecated
    protected boolean isElementEmpty(final int offset) {
        return this.getElementType(offset) == 0;
    }
    
    public boolean isEmpty() {
        return this.isElementEmpty(this._doc_start_offset + 4);
    }
    
    private int getBSONSize(final int offset) {
        return this._input.getInt(offset);
    }
    
    public int getBSONSize() {
        return this.getBSONSize(this._doc_start_offset);
    }
    
    public int pipe(final OutputStream os) throws IOException {
        os.write(this._input.array(), this._doc_start_offset, this.getBSONSize());
        return this.getBSONSize();
    }
    
    private String getElementFieldName(final int offset) {
        return this._input.getCString(offset);
    }
    
    @Deprecated
    protected byte getElementType(final int offset) {
        return this._input.get(offset);
    }
    
    @Deprecated
    protected int getElementBSONSize(int offset) {
        int x = 0;
        final byte type = this.getElementType(offset++);
        final int n = this.sizeCString(offset);
        final int valueOffset = offset + n;
        switch (type) {
            case -1:
            case 0:
            case 6:
            case 10:
            case Byte.MAX_VALUE: {
                break;
            }
            case 8: {
                x = 1;
                break;
            }
            case 16: {
                x = 4;
                break;
            }
            case 1:
            case 9:
            case 17:
            case 18: {
                x = 8;
                break;
            }
            case 7: {
                x = 12;
                break;
            }
            case 2:
            case 13:
            case 14: {
                x = this._input.getInt(valueOffset) + 4;
                break;
            }
            case 15: {
                x = this._input.getInt(valueOffset);
                break;
            }
            case 12: {
                x = this._input.getInt(valueOffset) + 4 + 12;
                break;
            }
            case 3:
            case 4: {
                x = this._input.getInt(valueOffset);
                break;
            }
            case 5: {
                x = this._input.getInt(valueOffset) + 4 + 1;
                break;
            }
            case 11: {
                final int part1 = this.sizeCString(valueOffset);
                final int part2 = this.sizeCString(valueOffset + part1);
                x = part1 + part2;
                break;
            }
            default: {
                throw new BSONException("Invalid type " + type + " for field " + this.getElementFieldName(offset));
            }
        }
        return x;
    }
    
    @Deprecated
    protected int sizeCString(final int offset) {
        int end = offset;
        while (true) {
            final byte b = this._input.get(end);
            if (b == 0) {
                break;
            }
            ++end;
        }
        return end - offset + 1;
    }
    
    @Deprecated
    protected Object getElementValue(final ElementRecord record) {
        switch (record.type) {
            case 0:
            case 6:
            case 10: {
                return null;
            }
            case Byte.MAX_VALUE: {
                return new MaxKey();
            }
            case -1: {
                return new MinKey();
            }
            case 8: {
                return this._input.get(record.valueOffset) != 0;
            }
            case 16: {
                return this._input.getInt(record.valueOffset);
            }
            case 17: {
                final int inc = this._input.getInt(record.valueOffset);
                final int time = this._input.getInt(record.valueOffset + 4);
                return new BSONTimestamp(time, inc);
            }
            case 9: {
                return new Date(this._input.getLong(record.valueOffset));
            }
            case 18: {
                return this._input.getLong(record.valueOffset);
            }
            case 1: {
                return Double.longBitsToDouble(this._input.getLong(record.valueOffset));
            }
            case 7: {
                return new ObjectId(this._input.getIntBE(record.valueOffset), this._input.getIntBE(record.valueOffset + 4), this._input.getIntBE(record.valueOffset + 8));
            }
            case 14: {
                return new Symbol(this._input.getUTF8String(record.valueOffset));
            }
            case 13: {
                return new Code(this._input.getUTF8String(record.valueOffset));
            }
            case 2: {
                return this._input.getUTF8String(record.valueOffset);
            }
            case 15: {
                final int strsize = this._input.getInt(record.valueOffset + 4);
                final String code = this._input.getUTF8String(record.valueOffset + 4);
                final BSONObject scope = (BSONObject)this._callback.createObject(this._input.array(), record.valueOffset + 4 + 4 + strsize);
                return new CodeWScope(code, scope);
            }
            case 12: {
                final int csize = this._input.getInt(record.valueOffset);
                final String ns = this._input.getCString(record.valueOffset + 4);
                final int oidOffset = record.valueOffset + csize + 4;
                final ObjectId oid = new ObjectId(this._input.getIntBE(oidOffset), this._input.getIntBE(oidOffset + 4), this._input.getIntBE(oidOffset + 8));
                return this._callback.createDBRef(ns, oid);
            }
            case 3: {
                return this._callback.createObject(this._input.array(), record.valueOffset);
            }
            case 4: {
                return this._callback.createArray(this._input.array(), record.valueOffset);
            }
            case 5: {
                return this.readBinary(record.valueOffset);
            }
            case 11: {
                final int patternCStringSize = this.sizeCString(record.valueOffset);
                final String pattern = this._input.getCString(record.valueOffset);
                final String flags = this._input.getCString(record.valueOffset + patternCStringSize);
                return Pattern.compile(pattern, BSON.regexFlags(flags));
            }
            default: {
                throw new BSONException("Invalid type " + record.type + " for field " + this.getElementFieldName(record.offset));
            }
        }
    }
    
    private Object readBinary(int valueOffset) {
        final int totalLen = this._input.getInt(valueOffset);
        valueOffset += 4;
        final byte bType = this._input.get(valueOffset);
        ++valueOffset;
        switch (bType) {
            case 0: {
                final byte[] bin = new byte[totalLen];
                for (int n = 0; n < totalLen; ++n) {
                    bin[n] = this._input.get(valueOffset + n);
                }
                return bin;
            }
            case 2: {
                final int len = this._input.getInt(valueOffset);
                if (len + 4 != totalLen) {
                    throw new IllegalArgumentException("Bad Data Size; Binary Subtype 2.  { actual len: " + len + " expected totalLen: " + totalLen + "}");
                }
                valueOffset += 4;
                final byte[] bin = new byte[len];
                for (int n2 = 0; n2 < len; ++n2) {
                    bin[n2] = this._input.get(valueOffset + n2);
                }
                return bin;
            }
            case 3: {
                if (totalLen != 16) {
                    throw new IllegalArgumentException("Bad Data Size; Binary Subtype 3 (UUID). { total length: " + totalLen + " != 16");
                }
                final long part1 = this._input.getLong(valueOffset);
                valueOffset += 8;
                final long part2 = this._input.getLong(valueOffset);
                return new UUID(part1, part2);
            }
            default: {
                final byte[] bin = new byte[totalLen];
                for (int n = 0; n < totalLen; ++n) {
                    bin[n] = this._input.get(valueOffset + n);
                }
                return bin;
            }
        }
    }
    
    protected int getOffset() {
        return this._doc_start_offset;
    }
    
    protected byte[] getBytes() {
        return this._input.array();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final LazyBSONObject that = (LazyBSONObject)o;
        return Arrays.equals(this._input.array(), that._input.array());
    }
    
    public int hashCode() {
        return Arrays.hashCode(this._input.array());
    }
    
    public String toString() {
        return JSON.serialize(this);
    }
    
    static {
        log = Logger.getLogger("org.bson.LazyBSONObject");
    }
    
    class ElementRecord
    {
        final String name;
        final byte type;
        final int fieldNameSize;
        final int valueOffset;
        final int offset;
        
        ElementRecord(final String name, final int offset) {
            super();
            this.name = name;
            this.offset = offset;
            this.type = LazyBSONObject.this.getElementType(offset - 1);
            this.fieldNameSize = LazyBSONObject.this.sizeCString(offset);
            this.valueOffset = offset + this.fieldNameSize;
        }
    }
    
    class LazyBSONKeyIterator implements Iterator<String>
    {
        int offset;
        
        LazyBSONKeyIterator() {
            super();
            this.offset = LazyBSONObject.this._doc_start_offset + 4;
        }
        
        public boolean hasNext() {
            return !LazyBSONObject.this.isElementEmpty(this.offset);
        }
        
        public String next() {
            final int fieldSize = LazyBSONObject.this.sizeCString(this.offset + 1);
            final int elementSize = LazyBSONObject.this.getElementBSONSize(this.offset);
            final String key = LazyBSONObject.this._input.getCString(this.offset + 1);
            this.offset += fieldSize + elementSize + 1;
            return key;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Read only");
        }
    }
    
    @Deprecated
    public class LazyBSONKeySet extends ReadOnlySet<String>
    {
        public int size() {
            int size = 0;
            final Iterator<String> iter = this.iterator();
            while (iter.hasNext()) {
                iter.next();
                ++size;
            }
            return size;
        }
        
        public boolean isEmpty() {
            return LazyBSONObject.this.isEmpty();
        }
        
        public boolean contains(final Object o) {
            for (final String key : this) {
                if (key.equals(o)) {
                    return true;
                }
            }
            return false;
        }
        
        public Iterator<String> iterator() {
            return new LazyBSONKeyIterator();
        }
        
        public String[] toArray() {
            final String[] a = new String[this.size()];
            return this.toArray(a);
        }
        
        public <T> T[] toArray(final T[] a) {
            final int size = this.size();
            final T[] localArray = (T[])((a.length >= size) ? a : ((Object[])Array.newInstance(a.getClass().getComponentType(), size)));
            int i = 0;
            for (final String key : this) {
                localArray[i++] = (T)key;
            }
            if (localArray.length > i) {
                localArray[i] = null;
            }
            return localArray;
        }
        
        public boolean add(final String e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        public boolean remove(final Object o) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        public boolean containsAll(final Collection<?> collection) {
            for (final Object item : collection) {
                if (!this.contains(item)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    class LazyBSONEntryIterator implements Iterator<Map.Entry<String, Object>>
    {
        int offset;
        
        LazyBSONEntryIterator() {
            super();
            this.offset = LazyBSONObject.this._doc_start_offset + 4;
        }
        
        public boolean hasNext() {
            return !LazyBSONObject.this.isElementEmpty(this.offset);
        }
        
        public Map.Entry<String, Object> next() {
            final int fieldSize = LazyBSONObject.this.sizeCString(this.offset + 1);
            final int elementSize = LazyBSONObject.this.getElementBSONSize(this.offset);
            final String key = LazyBSONObject.this._input.getCString(this.offset + 1);
            final ElementRecord nextElementRecord = new ElementRecord(key, ++this.offset);
            this.offset += fieldSize + elementSize;
            return new Map.Entry<String, Object>() {
                public String getKey() {
                    return nextElementRecord.name;
                }
                
                public Object getValue() {
                    return LazyBSONObject.this.getElementValue(nextElementRecord);
                }
                
                public Object setValue(final Object value) {
                    throw new UnsupportedOperationException("Read only");
                }
                
                public boolean equals(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry e = (Map.Entry)o;
                    return this.getKey().equals(e.getKey()) && this.getValue().equals(e.getValue());
                }
                
                public int hashCode() {
                    return this.getKey().hashCode() ^ this.getValue().hashCode();
                }
                
                public String toString() {
                    return this.getKey() + "=" + this.getValue();
                }
            };
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Read only");
        }
    }
    
    class LazyBSONEntrySet extends ReadOnlySet<Map.Entry<String, Object>>
    {
        public int size() {
            return LazyBSONObject.this.keySet().size();
        }
        
        public boolean isEmpty() {
            return LazyBSONObject.this.isEmpty();
        }
        
        public boolean contains(final Object o) {
            final Iterator<Map.Entry<String, Object>> iter = this.iterator();
            while (iter.hasNext()) {
                if (iter.next().equals(o)) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> c) {
            for (final Object cur : c) {
                if (!this.contains(cur)) {
                    return false;
                }
            }
            return true;
        }
        
        public Iterator<Map.Entry<String, Object>> iterator() {
            return new LazyBSONEntryIterator();
        }
        
        public Object[] toArray() {
            final Map.Entry[] array = new Map.Entry[this.size()];
            return this.toArray(array);
        }
        
        public <T> T[] toArray(final T[] a) {
            final int size = this.size();
            final T[] localArray = (T[])((a.length >= size) ? a : ((Object[])Array.newInstance(a.getClass().getComponentType(), size)));
            final Iterator<Map.Entry<String, Object>> iter = this.iterator();
            int i = 0;
            while (iter.hasNext()) {
                localArray[i++] = (T)iter.next();
            }
            if (localArray.length > i) {
                localArray[i] = null;
            }
            return localArray;
        }
    }
    
    abstract class ReadOnlySet<E> implements Set<E>
    {
        public boolean add(final E e) {
            throw new UnsupportedOperationException("Read-only Set");
        }
        
        public boolean remove(final Object o) {
            throw new UnsupportedOperationException("Read-only Set");
        }
        
        public boolean addAll(final Collection<? extends E> c) {
            throw new UnsupportedOperationException("Read-only Set");
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException("Read-only Set");
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException("Read-only Set");
        }
        
        public void clear() {
            throw new UnsupportedOperationException("Read-only Set");
        }
    }
}
