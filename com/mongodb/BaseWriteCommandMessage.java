package com.mongodb;

import org.bson.io.*;

abstract class BaseWriteCommandMessage extends RequestMessage
{
    private static final int HEADROOM = 16384;
    private final MongoNamespace writeNamespace;
    private final WriteConcern writeConcern;
    private final DBEncoder commandEncoder;
    
    public BaseWriteCommandMessage(final MongoNamespace writeNamespace, final WriteConcern writeConcern, final DBEncoder commandEncoder, final MessageSettings settings) {
        super(new MongoNamespace(writeNamespace.getDatabaseName(), "$cmd").getFullName(), OpCode.OP_QUERY, settings);
        this.writeNamespace = writeNamespace;
        this.writeConcern = writeConcern;
        this.commandEncoder = commandEncoder;
    }
    
    public MongoNamespace getWriteNamespace() {
        return this.writeNamespace;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public DBEncoder getCommandEncoder() {
        return this.commandEncoder;
    }
    
    public BaseWriteCommandMessage encode(final OutputBuffer buffer) {
        return (BaseWriteCommandMessage)super.encode(buffer);
    }
    
    protected BaseWriteCommandMessage encodeMessageBody(final OutputBuffer buffer, final int messageStartPosition) {
        BaseWriteCommandMessage nextMessage = null;
        this.writeCommandHeader(buffer);
        final int commandStartPosition = buffer.getPosition();
        final BSONBinaryWriter writer = new BSONBinaryWriter(new BSONWriterSettings(), new BSONBinaryWriterSettings(this.getSettings().getMaxDocumentSize() + 16384), buffer);
        try {
            writer.writeStartDocument();
            this.writeCommandPrologue(writer);
            nextMessage = this.writeTheWrites(buffer, commandStartPosition, writer);
            writer.writeEndDocument();
        }
        finally {
            writer.close();
        }
        return nextMessage;
    }
    
    private void writeCommandHeader(final OutputBuffer buffer) {
        buffer.writeInt(0);
        buffer.writeCString(this.getCollectionName());
        buffer.writeInt(0);
        buffer.writeInt(-1);
    }
    
    protected abstract String getCommandName();
    
    protected abstract BaseWriteCommandMessage writeTheWrites(final OutputBuffer p0, final int p1, final BSONBinaryWriter p2);
    
    protected boolean exceedsLimits(final int batchLength, final int batchItemCount) {
        return this.exceedsBatchLengthLimit(batchLength, batchItemCount) || this.exceedsBatchItemCountLimit(batchItemCount);
    }
    
    private boolean exceedsBatchLengthLimit(final int batchLength, final int batchItemCount) {
        return batchLength > this.getSettings().getMaxDocumentSize() && batchItemCount > 1;
    }
    
    private boolean exceedsBatchItemCountLimit(final int batchItemCount) {
        return batchItemCount > this.getSettings().getMaxWriteBatchSize();
    }
    
    public abstract int getItemCount();
    
    private void writeCommandPrologue(final BSONBinaryWriter writer) {
        writer.writeString(this.getCommandName(), this.getWriteNamespace().getCollectionName());
        writer.writeBoolean("ordered", !this.getWriteConcern().getContinueOnError());
        if (!this.getWriteConcern().useServerDefault()) {
            writer.writeName("writeConcern");
            writer.encodeDocument(this.getCommandEncoder(), this.getWriteConcern().asDBObject());
        }
    }
}
