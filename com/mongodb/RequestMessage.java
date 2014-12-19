package com.mongodb;

import java.util.concurrent.atomic.*;
import org.bson.io.*;

abstract class RequestMessage
{
    static final AtomicInteger REQUEST_ID;
    private final String collectionName;
    private MessageSettings settings;
    private final int id;
    private final OpCode opCode;
    
    public RequestMessage(final String collectionName, final OpCode opCode, final MessageSettings settings) {
        super();
        this.collectionName = collectionName;
        this.settings = settings;
        this.id = RequestMessage.REQUEST_ID.getAndIncrement();
        this.opCode = opCode;
    }
    
    protected void writeMessagePrologue(final OutputBuffer buffer) {
        buffer.writeInt(0);
        buffer.writeInt(this.id);
        buffer.writeInt(0);
        buffer.writeInt(this.opCode.getValue());
    }
    
    public int getId() {
        return this.id;
    }
    
    public OpCode getOpCode() {
        return this.opCode;
    }
    
    public String getNamespace() {
        return (this.getCollectionName() != null) ? this.getCollectionName() : null;
    }
    
    public MessageSettings getSettings() {
        return this.settings;
    }
    
    public RequestMessage encode(final OutputBuffer buffer) {
        final int messageStartPosition = buffer.getPosition();
        this.writeMessagePrologue(buffer);
        final RequestMessage nextMessage = this.encodeMessageBody(buffer, messageStartPosition);
        this.backpatchMessageLength(messageStartPosition, buffer);
        return nextMessage;
    }
    
    protected abstract RequestMessage encodeMessageBody(final OutputBuffer p0, final int p1);
    
    protected void backpatchMessageLength(final int startPosition, final OutputBuffer buffer) {
        final int messageLength = buffer.getPosition() - startPosition;
        buffer.backpatchSize(messageLength);
    }
    
    protected String getCollectionName() {
        return this.collectionName;
    }
    
    static {
        REQUEST_ID = new AtomicInteger(1);
    }
    
    enum OpCode
    {
        OP_REPLY(1), 
        OP_MSG(1000), 
        OP_UPDATE(2001), 
        OP_INSERT(2002), 
        OP_QUERY(2004), 
        OP_GETMORE(2005), 
        OP_DELETE(2006), 
        OP_KILL_CURSORS(2007);
        
        private final int value;
        
        private OpCode(final int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
}
