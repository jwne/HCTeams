package com.mongodb;

import java.util.*;

final class WriteCommandResultHelper
{
    static boolean hasError(final CommandResult commandResult) {
        return commandResult.get("writeErrors") != null || commandResult.get("writeConcernError") != null;
    }
    
    static BulkWriteResult getBulkWriteResult(final WriteRequest.Type type, final CommandResult commandResult) {
        final int count = getCount(commandResult);
        final List<BulkWriteUpsert> upsertedItems = getUpsertedItems(commandResult);
        return new AcknowledgedBulkWriteResult(type, count - upsertedItems.size(), getModifiedCount(type, commandResult), upsertedItems);
    }
    
    static BulkWriteException getBulkWriteException(final WriteRequest.Type type, final CommandResult commandResult) {
        if (!hasError(commandResult)) {
            throw new MongoInternalException("This method should not have been called");
        }
        return new BulkWriteException(getBulkWriteResult(type, commandResult), getWriteErrors(commandResult), getWriteConcernError(commandResult), commandResult.getServerUsed());
    }
    
    private static List<BulkWriteError> getWriteErrors(final CommandResult commandResult) {
        final List<BulkWriteError> writeErrors = new ArrayList<BulkWriteError>();
        final List<DBObject> writeErrorsDocuments = (List<DBObject>)commandResult.get("writeErrors");
        if (writeErrorsDocuments != null) {
            for (final DBObject cur : writeErrorsDocuments) {
                writeErrors.add(new BulkWriteError((int)cur.get("code"), (String)cur.get("errmsg"), getErrInfo(cur), (int)cur.get("index")));
            }
        }
        return writeErrors;
    }
    
    private static WriteConcernError getWriteConcernError(final CommandResult commandResult) {
        final DBObject writeConcernErrorDocument = (DBObject)commandResult.get("writeConcernError");
        if (writeConcernErrorDocument == null) {
            return null;
        }
        return new WriteConcernError((int)writeConcernErrorDocument.get("code"), (String)writeConcernErrorDocument.get("errmsg"), getErrInfo(writeConcernErrorDocument));
    }
    
    private static List<BulkWriteUpsert> getUpsertedItems(final CommandResult commandResult) {
        final List<DBObject> upsertedValue = (List<DBObject>)commandResult.get("upserted");
        if (upsertedValue == null) {
            return Collections.emptyList();
        }
        final List<BulkWriteUpsert> bulkWriteUpsertList = new ArrayList<BulkWriteUpsert>();
        for (final DBObject upsertedItem : upsertedValue) {
            bulkWriteUpsertList.add(new BulkWriteUpsert(((Number)upsertedItem.get("index")).intValue(), upsertedItem.get("_id")));
        }
        return bulkWriteUpsertList;
    }
    
    private static int getCount(final CommandResult commandResult) {
        return commandResult.getInt("n");
    }
    
    private static Integer getModifiedCount(final WriteRequest.Type type, final CommandResult commandResult) {
        Integer modifiedCount = (Integer)commandResult.get("nModified");
        if (modifiedCount == null && type != WriteRequest.Type.UPDATE && type != WriteRequest.Type.REPLACE) {
            modifiedCount = 0;
        }
        return modifiedCount;
    }
    
    private static DBObject getErrInfo(final DBObject response) {
        final DBObject errInfo = (DBObject)response.get("errInfo");
        return (errInfo != null) ? errInfo : new BasicDBObject();
    }
}
