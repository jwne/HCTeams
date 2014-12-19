package com.mongodb;

import java.util.*;
import org.bson.io.*;

class UpdateCommandMessage extends BaseWriteCommandMessage
{
    private final List<ModifyRequest> updates;
    private final DBEncoder encoder;
    
    public UpdateCommandMessage(final MongoNamespace writeNamespace, final WriteConcern writeConcern, final List<ModifyRequest> updates, final DBEncoder commandEncoder, final DBEncoder encoder, final MessageSettings settings) {
        super(writeNamespace, writeConcern, commandEncoder, settings);
        this.updates = updates;
        this.encoder = encoder;
    }
    
    protected UpdateCommandMessage writeTheWrites(final OutputBuffer buffer, final int commandStartPosition, final BSONBinaryWriter writer) {
        UpdateCommandMessage nextMessage = null;
        writer.writeStartArray("updates");
        for (int i = 0; i < this.updates.size(); ++i) {
            writer.mark();
            final ModifyRequest update = this.updates.get(i);
            writer.writeStartDocument();
            writer.pushMaxDocumentSize(this.getSettings().getMaxDocumentSize());
            writer.writeName("q");
            writer.encodeDocument(this.getCommandEncoder(), update.getQuery());
            writer.writeName("u");
            writer.encodeDocument(this.encoder, update.getUpdateDocument());
            if (update.isMulti()) {
                writer.writeBoolean("multi", update.isMulti());
            }
            if (update.isUpsert()) {
                writer.writeBoolean("upsert", update.isUpsert());
            }
            writer.popMaxDocumentSize();
            writer.writeEndDocument();
            if (this.exceedsLimits(buffer.getPosition() - commandStartPosition, i + 1)) {
                writer.reset();
                nextMessage = new UpdateCommandMessage(this.getWriteNamespace(), this.getWriteConcern(), this.updates.subList(i, this.updates.size()), this.getCommandEncoder(), this.encoder, this.getSettings());
                break;
            }
        }
        writer.writeEndArray();
        return nextMessage;
    }
    
    public int getItemCount() {
        return this.updates.size();
    }
    
    protected String getCommandName() {
        return "update";
    }
}
