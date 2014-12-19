package com.mongodb;

import java.io.*;
import org.bson.*;
import org.bson.types.*;
import java.util.*;

abstract class BSONWriter implements Closeable
{
    private final BSONWriterSettings settings;
    private State state;
    private Context context;
    private String currentName;
    private int serializationDepth;
    private boolean closed;
    
    protected BSONWriter(final BSONWriterSettings settings) {
        super();
        this.settings = settings;
        this.state = State.INITIAL;
    }
    
    protected String getName() {
        return this.currentName;
    }
    
    protected boolean isClosed() {
        return this.closed;
    }
    
    protected void setState(final State state) {
        this.state = state;
    }
    
    protected State getState() {
        return this.state;
    }
    
    protected Context getContext() {
        return this.context;
    }
    
    protected void setContext(final Context context) {
        this.context = context;
    }
    
    public abstract void flush();
    
    public abstract void writeBinaryData(final Binary p0);
    
    public void writeBinaryData(final String name, final Binary binary) {
        this.writeName(name);
        this.writeBinaryData(binary);
    }
    
    public abstract void writeBoolean(final boolean p0);
    
    public void writeBoolean(final String name, final boolean value) {
        this.writeName(name);
        this.writeBoolean(value);
    }
    
    public abstract void writeDateTime(final long p0);
    
    public void writeDateTime(final String name, final long value) {
        this.writeName(name);
        this.writeDateTime(value);
    }
    
    public abstract void writeDouble(final double p0);
    
    public void writeDouble(final String name, final double value) {
        this.writeName(name);
        this.writeDouble(value);
    }
    
    public void writeEndArray() {
        --this.serializationDepth;
    }
    
    public void writeEndDocument() {
        --this.serializationDepth;
    }
    
    public abstract void writeInt32(final int p0);
    
    public void writeInt32(final String name, final int value) {
        this.writeName(name);
        this.writeInt32(value);
    }
    
    public abstract void writeInt64(final long p0);
    
    public void writeInt64(final String name, final long value) {
        this.writeName(name);
        this.writeInt64(value);
    }
    
    public abstract void writeJavaScript(final String p0);
    
    public void writeJavaScript(final String name, final String code) {
        this.writeName(name);
        this.writeJavaScript(code);
    }
    
    public abstract void writeJavaScriptWithScope(final String p0);
    
    public void writeJavaScriptWithScope(final String name, final String code) {
        this.writeName(name);
        this.writeJavaScriptWithScope(code);
    }
    
    public abstract void writeMaxKey();
    
    public void writeMaxKey(final String name) {
        this.writeName(name);
        this.writeMaxKey();
    }
    
    public abstract void writeMinKey();
    
    public void writeMinKey(final String name) {
        this.writeName(name);
        this.writeMinKey();
    }
    
    public void writeName(final String name) {
        if (this.state != State.NAME) {
            this.throwInvalidState("WriteName", State.NAME);
        }
        this.currentName = name;
        this.state = State.VALUE;
    }
    
    public abstract void writeNull();
    
    public void writeNull(final String name) {
        this.writeName(name);
        this.writeNull();
    }
    
    public abstract void writeObjectId(final ObjectId p0);
    
    public void writeObjectId(final String name, final ObjectId objectId) {
        this.writeName(name);
        this.writeObjectId(objectId);
    }
    
    public void writeStartArray() {
        ++this.serializationDepth;
        if (this.serializationDepth > this.settings.getMaxSerializationDepth()) {
            throw new BSONException("Maximum serialization depth exceeded (does the object being serialized have a circular reference?).");
        }
    }
    
    public void writeStartArray(final String name) {
        this.writeName(name);
        this.writeStartArray();
    }
    
    public void writeStartDocument() {
        ++this.serializationDepth;
        if (this.serializationDepth > this.settings.getMaxSerializationDepth()) {
            throw new BSONException("Maximum serialization depth exceeded (does the object being serialized have a circular reference?).");
        }
    }
    
    public void writeStartDocument(final String name) {
        this.writeName(name);
        this.writeStartDocument();
    }
    
    public abstract void writeString(final String p0);
    
    public void writeString(final String name, final String value) {
        this.writeName(name);
        this.writeString(value);
    }
    
    public abstract void writeSymbol(final String p0);
    
    public void writeSymbol(final String name, final String value) {
        this.writeName(name);
        this.writeSymbol(value);
    }
    
    public abstract void writeTimestamp(final BSONTimestamp p0);
    
    public void writeTimestamp(final String name, final BSONTimestamp value) {
        this.writeName(name);
        this.writeTimestamp(value);
    }
    
    public abstract void writeUndefined();
    
    public void writeUndefined(final String name) {
        this.writeName(name);
        this.writeUndefined();
    }
    
    protected State getNextState() {
        if (this.getContext().getContextType() == BSONContextType.ARRAY) {
            return State.VALUE;
        }
        return State.NAME;
    }
    
    protected boolean checkState(final State[] validStates) {
        for (final State cur : validStates) {
            if (cur == this.getState()) {
                return true;
            }
        }
        return false;
    }
    
    protected void checkPreconditions(final String methodName, final State... validStates) {
        if (this.isClosed()) {
            throw new IllegalStateException("BSONWriter");
        }
        if (!this.checkState(validStates)) {
            this.throwInvalidState(methodName, validStates);
        }
    }
    
    protected void throwInvalidContextType(final String methodName, final BSONContextType actualContextType, final BSONContextType... validContextTypes) {
        final String validContextTypesString = StringUtils.join(" or ", Arrays.asList(validContextTypes));
        final String message = String.format("%s can only be called when ContextType is %s, not when ContextType is %s.", methodName, validContextTypesString, actualContextType);
        throw new BSONException(message);
    }
    
    protected void throwInvalidState(final String methodName, final State... validStates) {
        if ((this.state == State.INITIAL || this.state == State.SCOPE_DOCUMENT || this.state == State.DONE) && !methodName.startsWith("end") && !methodName.equals("writeName")) {
            String typeName = methodName.substring(5);
            if (typeName.startsWith("start")) {
                typeName = typeName.substring(5);
            }
            String article = "A";
            if (Arrays.asList('A', 'E', 'I', 'O', 'U').contains(typeName.charAt(0))) {
                article = "An";
            }
            final String message = String.format("%s %s value cannot be written to the root level of a BSON document.", article, typeName);
            throw new BSONException(message);
        }
        final String validStatesString = StringUtils.join(" or ", Arrays.asList(validStates));
        final String message = String.format("%s can only be called when State is %s, not when State is %s", methodName, validStatesString, this.state);
        throw new BSONException(message);
    }
    
    public void close() {
        this.closed = true;
    }
    
    enum State
    {
        INITIAL, 
        NAME, 
        VALUE, 
        SCOPE_DOCUMENT, 
        DONE, 
        CLOSED;
    }
    
    class Context
    {
        private final Context parentContext;
        private final BSONContextType contextType;
        
        public Context(final Context from) {
            super();
            this.parentContext = from.parentContext;
            this.contextType = from.contextType;
        }
        
        public Context(final Context parentContext, final BSONContextType contextType) {
            super();
            this.parentContext = parentContext;
            this.contextType = contextType;
        }
        
        public Context getParentContext() {
            return this.parentContext;
        }
        
        public BSONContextType getContextType() {
            return this.contextType;
        }
        
        public Context copy() {
            return new Context(this);
        }
    }
    
    class Mark
    {
        private final Context markedContext;
        private final State markedState;
        private final String currentName;
        private final int serializationDepth;
        
        protected Mark() {
            super();
            this.markedContext = BSONWriter.this.context.copy();
            this.markedState = BSONWriter.this.state;
            this.currentName = BSONWriter.this.currentName;
            this.serializationDepth = BSONWriter.this.serializationDepth;
        }
        
        protected void reset() {
            BSONWriter.this.setContext(this.markedContext);
            BSONWriter.this.setState(this.markedState);
            BSONWriter.this.currentName = this.currentName;
            BSONWriter.this.serializationDepth = this.serializationDepth;
        }
    }
}
