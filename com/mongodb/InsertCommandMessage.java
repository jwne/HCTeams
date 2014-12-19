package com.mongodb;

import java.util.*;
import org.bson.io.*;

class InsertCommandMessage extends BaseWriteCommandMessage
{
    private final List<DBObject> documents;
    private final DBEncoder encoder;
    
    public InsertCommandMessage(final MongoNamespace namespace, final WriteConcern writeConcern, final List<DBObject> documents, final DBEncoder commandEncoder, final DBEncoder encoder, final MessageSettings settings) {
        super(namespace, writeConcern, commandEncoder, settings);
        this.documents = documents;
        this.encoder = encoder;
    }
    
    protected String getCommandName() {
        return "insert";
    }
    
    protected InsertCommandMessage writeTheWrites(final OutputBuffer buffer, final int commandStartPosition, final BSONBinaryWriter writer) {
        InsertCommandMessage nextMessage = null;
        writer.writeStartArray("documents");
        writer.pushMaxDocumentSize(this.getSettings().getMaxDocumentSize());
        for (int i = 0; i < this.documents.size(); ++i) {
            writer.mark();
            writer.encodeDocument(this.encoder, this.documents.get(i));
            if (this.exceedsLimits(buffer.getPosition() - commandStartPosition, i + 1)) {
                writer.reset();
                nextMessage = new InsertCommandMessage(this.getWriteNamespace(), this.getWriteConcern(), this.documents.subList(i, this.documents.size()), this.getCommandEncoder(), this.encoder, this.getSettings());
                break;
            }
        }
        writer.popMaxDocumentSize();
        writer.writeEndArray();
        return nextMessage;
    }
    
    public int getItemCount() {
        return this.documents.size();
    }
}
