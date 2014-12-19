package com.mongodb;

import org.bson.io.*;
import java.util.*;
import org.bson.types.*;
import org.bson.util.*;
import org.bson.*;

class BSONBinaryWriter extends BSONWriter
{
    private final BSONBinaryWriterSettings binaryWriterSettings;
    private final OutputBuffer buffer;
    private final Stack<Integer> maxDocumentSizeStack;
    private Mark mark;
    
    public BSONBinaryWriter(final OutputBuffer buffer) {
        this(new BSONWriterSettings(), new BSONBinaryWriterSettings(), buffer);
    }
    
    public BSONBinaryWriter(final BSONWriterSettings settings, final BSONBinaryWriterSettings binaryWriterSettings, final OutputBuffer buffer) {
        super(settings);
        this.maxDocumentSizeStack = new Stack<Integer>();
        this.binaryWriterSettings = binaryWriterSettings;
        this.buffer = buffer;
        this.maxDocumentSizeStack.push(binaryWriterSettings.getMaxDocumentSize());
    }
    
    public void close() {
        super.close();
    }
    
    public OutputBuffer getBuffer() {
        return this.buffer;
    }
    
    public void flush() {
    }
    
    protected Context getContext() {
        return (Context)super.getContext();
    }
    
    public void writeBinaryData(final Binary binary) {
        this.checkPreconditions("writeBinaryData", State.VALUE);
        this.buffer.write(BSONType.BINARY.getValue());
        this.writeCurrentName();
        int totalLen = binary.length();
        if (binary.getType() == BSONBinarySubType.OldBinary.getValue()) {
            totalLen += 4;
        }
        this.buffer.writeInt(totalLen);
        this.buffer.write(binary.getType());
        if (binary.getType() == BSONBinarySubType.OldBinary.getValue()) {
            this.buffer.writeInt(totalLen - 4);
        }
        this.buffer.write(binary.getData());
        this.setState(this.getNextState());
    }
    
    public void writeBoolean(final boolean value) {
        this.checkPreconditions("writeBoolean", State.VALUE);
        this.buffer.write(BSONType.BOOLEAN.getValue());
        this.writeCurrentName();
        this.buffer.write(value ? 1 : 0);
        this.setState(this.getNextState());
    }
    
    public void writeDateTime(final long value) {
        this.checkPreconditions("writeDateTime", State.VALUE);
        this.buffer.write(BSONType.DATE_TIME.getValue());
        this.writeCurrentName();
        this.buffer.writeLong(value);
        this.setState(this.getNextState());
    }
    
    public void writeDouble(final double value) {
        this.checkPreconditions("writeDouble", State.VALUE);
        this.buffer.write(BSONType.DOUBLE.getValue());
        this.writeCurrentName();
        this.buffer.writeDouble(value);
        this.setState(this.getNextState());
    }
    
    public void writeInt32(final int value) {
        this.checkPreconditions("writeInt32", State.VALUE);
        this.buffer.write(BSONType.INT32.getValue());
        this.writeCurrentName();
        this.buffer.writeInt(value);
        this.setState(this.getNextState());
    }
    
    public void writeInt64(final long value) {
        this.checkPreconditions("writeInt64", State.VALUE);
        this.buffer.write(BSONType.INT64.getValue());
        this.writeCurrentName();
        this.buffer.writeLong(value);
        this.setState(this.getNextState());
    }
    
    public void writeJavaScript(final String code) {
        this.checkPreconditions("writeJavaScript", State.VALUE);
        this.buffer.write(BSONType.JAVASCRIPT.getValue());
        this.writeCurrentName();
        this.buffer.writeString(code);
        this.setState(this.getNextState());
    }
    
    public void writeJavaScriptWithScope(final String code) {
        this.checkPreconditions("writeJavaScriptWithScope", State.VALUE);
        this.buffer.write(BSONType.JAVASCRIPT_WITH_SCOPE.getValue());
        this.writeCurrentName();
        this.setContext(new Context(this.getContext(), BSONContextType.JAVASCRIPT_WITH_SCOPE, this.buffer.getPosition()));
        this.buffer.writeInt(0);
        this.buffer.writeString(code);
        this.setState(State.SCOPE_DOCUMENT);
    }
    
    public void writeMaxKey() {
        this.checkPreconditions("writeMaxKey", State.VALUE);
        this.buffer.write(BSONType.MAX_KEY.getValue());
        this.writeCurrentName();
        this.setState(this.getNextState());
    }
    
    public void writeMinKey() {
        this.checkPreconditions("writeMinKey", State.VALUE);
        this.buffer.write(BSONType.MIN_KEY.getValue());
        this.writeCurrentName();
        this.setState(this.getNextState());
    }
    
    public void writeNull() {
        this.checkPreconditions("writeNull", State.VALUE);
        this.buffer.write(BSONType.NULL.getValue());
        this.writeCurrentName();
        this.setState(this.getNextState());
    }
    
    public void writeObjectId(final ObjectId objectId) {
        this.checkPreconditions("writeObjectId", State.VALUE);
        this.buffer.write(BSONType.OBJECT_ID.getValue());
        this.writeCurrentName();
        this.buffer.write(objectId.toByteArray());
        this.setState(this.getNextState());
    }
    
    public void writeString(final String value) {
        this.checkPreconditions("writeString", State.VALUE);
        this.buffer.write(BSONType.STRING.getValue());
        this.writeCurrentName();
        this.buffer.writeString(value);
        this.setState(this.getNextState());
    }
    
    public void writeSymbol(final String value) {
        this.checkPreconditions("writeSymbol", State.VALUE);
        this.buffer.write(BSONType.SYMBOL.getValue());
        this.writeCurrentName();
        this.buffer.writeString(value);
        this.setState(this.getNextState());
    }
    
    public void writeTimestamp(final BSONTimestamp value) {
        this.checkPreconditions("writeTimestamp", State.VALUE);
        this.buffer.write(BSONType.TIMESTAMP.getValue());
        this.writeCurrentName();
        this.buffer.writeInt(value.getInc());
        this.buffer.writeInt(value.getTime());
        this.setState(this.getNextState());
    }
    
    public void writeUndefined() {
        this.checkPreconditions("writeUndefined", State.VALUE);
        this.buffer.write(BSONType.UNDEFINED.getValue());
        this.writeCurrentName();
        this.setState(this.getNextState());
    }
    
    public void writeStartArray() {
        this.checkPreconditions("writeStartArray", State.VALUE);
        super.writeStartArray();
        this.buffer.write(BSONType.ARRAY.getValue());
        this.writeCurrentName();
        this.setContext(new Context(this.getContext(), BSONContextType.ARRAY, this.buffer.getPosition()));
        this.buffer.writeInt(0);
        this.setState(State.VALUE);
    }
    
    public void writeStartDocument() {
        this.checkPreconditions("writeStartDocument", State.INITIAL, State.VALUE, State.SCOPE_DOCUMENT, State.DONE);
        super.writeStartDocument();
        if (this.getState() == State.VALUE) {
            this.buffer.write(BSONType.DOCUMENT.getValue());
            this.writeCurrentName();
        }
        this.setContext(new Context(this.getContext(), BSONContextType.DOCUMENT, this.buffer.getPosition()));
        this.buffer.writeInt(0);
        this.setState(State.NAME);
    }
    
    public void writeEndArray() {
        this.checkPreconditions("writeEndArray", State.VALUE);
        if (this.getContext().getContextType() != BSONContextType.ARRAY) {
            this.throwInvalidContextType("WriteEndArray", this.getContext().getContextType(), BSONContextType.ARRAY);
        }
        super.writeEndArray();
        this.buffer.write(0);
        this.backpatchSize();
        this.setContext(this.getContext().getParentContext());
        this.setState(this.getNextState());
    }
    
    public void writeEndDocument() {
        this.checkPreconditions("writeEndDocument", State.NAME);
        final BSONContextType contextType = this.getContext().getContextType();
        if (contextType != BSONContextType.DOCUMENT && contextType != BSONContextType.SCOPE_DOCUMENT) {
            this.throwInvalidContextType("WriteEndDocument", contextType, BSONContextType.DOCUMENT, BSONContextType.SCOPE_DOCUMENT);
        }
        super.writeEndDocument();
        this.buffer.write(0);
        this.backpatchSize();
        this.setContext(this.getContext().getParentContext());
        if (this.getContext() == null) {
            this.setState(State.DONE);
        }
        else {
            if (this.getContext().getContextType() == BSONContextType.JAVASCRIPT_WITH_SCOPE) {
                this.backpatchSize();
                this.setContext(this.getContext().getParentContext());
            }
            this.setState(this.getNextState());
        }
    }
    
    public void encodeDocument(final DBEncoder encoder, final DBObject dbObject) {
        this.checkPreconditions("writeStartDocument", State.INITIAL, State.VALUE, State.SCOPE_DOCUMENT, State.DONE);
        Assertions.isTrue("state is VALUE", this.getState() == State.VALUE);
        Assertions.isTrue("context not null", this.getContext() != null);
        Assertions.isTrue("context is not JAVASCRIPT_WITH_SCOPE", this.getContext().getContextType() != BSONContextType.JAVASCRIPT_WITH_SCOPE);
        this.buffer.write(BSONType.DOCUMENT.getValue());
        this.writeCurrentName();
        final int startPos = this.buffer.getPosition();
        encoder.writeObject(this.buffer, dbObject);
        this.throwIfSizeExceedsLimit(this.buffer.getPosition() - startPos);
        this.setState(this.getNextState());
    }
    
    public void pushMaxDocumentSize(final int maxDocumentSize) {
        this.maxDocumentSizeStack.push(maxDocumentSize);
    }
    
    public void popMaxDocumentSize() {
        this.maxDocumentSizeStack.pop();
    }
    
    public void mark() {
        this.mark = new Mark();
    }
    
    public void reset() {
        if (this.mark == null) {
            throw new IllegalStateException("Can not reset without first marking");
        }
        this.mark.reset();
        this.mark = null;
    }
    
    private void writeCurrentName() {
        if (this.getContext().getContextType() == BSONContextType.ARRAY) {
            this.buffer.writeCString(Integer.toString(this.getContext().index++));
        }
        else {
            this.buffer.writeCString(this.getName());
        }
    }
    
    private void backpatchSize() {
        final int size = this.buffer.getPosition() - this.getContext().startPosition;
        this.throwIfSizeExceedsLimit(size);
        this.buffer.backpatchSize(size);
    }
    
    private void throwIfSizeExceedsLimit(final int size) {
        if (size > this.maxDocumentSizeStack.peek()) {
            final String message = String.format("Size %d is larger than MaxDocumentSize %d.", this.buffer.getPosition() - this.getContext().startPosition, this.binaryWriterSettings.getMaxDocumentSize());
            throw new MongoInternalException(message);
        }
    }
    
    class Context extends BSONWriter.Context
    {
        private final int startPosition;
        private int index;
        
        public Context(final Context parentContext, final BSONContextType contextType, final int startPosition) {
            super(parentContext, contextType);
            this.startPosition = startPosition;
        }
        
        public Context(final Context from) {
            super(from);
            this.startPosition = from.startPosition;
            this.index = from.index;
        }
        
        public Context getParentContext() {
            return (Context)super.getParentContext();
        }
        
        public Context copy() {
            return new Context(this);
        }
    }
    
    class Mark extends BSONWriter.Mark
    {
        private final int position;
        
        protected Mark() {
            super();
            this.position = BSONBinaryWriter.this.buffer.getPosition();
        }
        
        protected void reset() {
            super.reset();
            BSONBinaryWriter.this.buffer.truncateToPosition(BSONBinaryWriter.this.mark.position);
        }
    }
}
