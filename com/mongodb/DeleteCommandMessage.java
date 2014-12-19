package com.mongodb;

import java.util.*;
import org.bson.io.*;

class DeleteCommandMessage extends BaseWriteCommandMessage
{
    private final List<RemoveRequest> deletes;
    private final DBEncoder queryEncoder;
    
    public DeleteCommandMessage(final MongoNamespace namespace, final WriteConcern writeConcern, final List<RemoveRequest> deletes, final DBEncoder commandEncoder, final DBEncoder queryEncoder, final MessageSettings settings) {
        super(namespace, writeConcern, commandEncoder, settings);
        this.deletes = deletes;
        this.queryEncoder = queryEncoder;
    }
    
    protected String getCommandName() {
        return "delete";
    }
    
    protected BaseWriteCommandMessage writeTheWrites(final OutputBuffer buffer, final int commandStartPosition, final BSONBinaryWriter writer) {
        DeleteCommandMessage nextMessage = null;
        writer.writeStartArray("deletes");
        for (int i = 0; i < this.deletes.size(); ++i) {
            writer.mark();
            final RemoveRequest remove = this.deletes.get(i);
            writer.writeStartDocument();
            writer.pushMaxDocumentSize(this.getSettings().getMaxDocumentSize());
            writer.writeName("q");
            writer.encodeDocument(this.getCommandEncoder(), remove.getQuery());
            writer.writeInt32("limit", remove.isMulti() ? 0 : 1);
            writer.popMaxDocumentSize();
            writer.writeEndDocument();
            if (this.exceedsLimits(buffer.getPosition() - commandStartPosition, i + 1)) {
                writer.reset();
                nextMessage = new DeleteCommandMessage(this.getWriteNamespace(), this.getWriteConcern(), this.deletes.subList(i, this.deletes.size()), this.getCommandEncoder(), this.queryEncoder, this.getSettings());
                break;
            }
        }
        writer.writeEndArray();
        return nextMessage;
    }
    
    public int getItemCount() {
        return this.deletes.size();
    }
}
