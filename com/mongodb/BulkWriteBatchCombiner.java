package com.mongodb;

import org.bson.util.*;
import java.util.*;

class BulkWriteBatchCombiner
{
    private final ServerAddress serverAddress;
    private final boolean ordered;
    private final WriteConcern writeConcern;
    private int insertedCount;
    private int matchedCount;
    private int removedCount;
    private Integer modifiedCount;
    private final Set<BulkWriteUpsert> writeUpserts;
    private final Set<BulkWriteError> writeErrors;
    private final List<WriteConcernError> writeConcernErrors;
    
    public BulkWriteBatchCombiner(final ServerAddress serverAddress, final WriteConcern writeConcern) {
        super();
        this.modifiedCount = 0;
        this.writeUpserts = new TreeSet<BulkWriteUpsert>(new Comparator<BulkWriteUpsert>() {
            public int compare(final BulkWriteUpsert o1, final BulkWriteUpsert o2) {
                return (o1.getIndex() < o2.getIndex()) ? -1 : ((o1.getIndex() == o2.getIndex()) ? 0 : 1);
            }
        });
        this.writeErrors = new TreeSet<BulkWriteError>(new Comparator<BulkWriteError>() {
            public int compare(final BulkWriteError o1, final BulkWriteError o2) {
                return (o1.getIndex() < o2.getIndex()) ? -1 : ((o1.getIndex() == o2.getIndex()) ? 0 : 1);
            }
        });
        this.writeConcernErrors = new ArrayList<WriteConcernError>();
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.ordered = !writeConcern.getContinueOnError();
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
    }
    
    public void addResult(final BulkWriteResult result, final IndexMap indexMap) {
        this.insertedCount += result.getInsertedCount();
        this.matchedCount += result.getMatchedCount();
        this.removedCount += result.getRemovedCount();
        if (result.isModifiedCountAvailable() && this.modifiedCount != null) {
            this.modifiedCount += result.getModifiedCount();
        }
        else {
            this.modifiedCount = null;
        }
        this.mergeUpserts(result.getUpserts(), indexMap);
    }
    
    public void addErrorResult(final BulkWriteException exception, final IndexMap indexMap) {
        this.addResult(exception.getWriteResult(), indexMap);
        this.mergeWriteErrors(exception.getWriteErrors(), indexMap);
        this.mergeWriteConcernError(exception.getWriteConcernError());
    }
    
    public void addWriteErrorResult(final BulkWriteError writeError, final IndexMap indexMap) {
        Assertions.notNull("writeError", writeError);
        this.mergeWriteErrors(Arrays.asList(writeError), indexMap);
    }
    
    public void addWriteConcernErrorResult(final WriteConcernError writeConcernError) {
        Assertions.notNull("writeConcernError", writeConcernError);
        this.mergeWriteConcernError(writeConcernError);
    }
    
    public void addErrorResult(final List<BulkWriteError> writeErrors, final WriteConcernError writeConcernError, final IndexMap indexMap) {
        this.mergeWriteErrors(writeErrors, indexMap);
        this.mergeWriteConcernError(writeConcernError);
    }
    
    private void mergeWriteConcernError(final WriteConcernError writeConcernError) {
        if (writeConcernError != null) {
            if (this.writeConcernErrors.isEmpty()) {
                this.writeConcernErrors.add(writeConcernError);
            }
            else if (!writeConcernError.equals(this.writeConcernErrors.get(this.writeConcernErrors.size() - 1))) {
                this.writeConcernErrors.add(writeConcernError);
            }
        }
    }
    
    private void mergeWriteErrors(final List<BulkWriteError> newWriteErrors, final IndexMap indexMap) {
        for (final BulkWriteError cur : newWriteErrors) {
            this.writeErrors.add(new BulkWriteError(cur.getCode(), cur.getMessage(), cur.getDetails(), indexMap.map(cur.getIndex())));
        }
    }
    
    private void mergeUpserts(final List<BulkWriteUpsert> upserts, final IndexMap indexMap) {
        for (final BulkWriteUpsert bulkWriteUpsert : upserts) {
            this.writeUpserts.add(new BulkWriteUpsert(indexMap.map(bulkWriteUpsert.getIndex()), bulkWriteUpsert.getId()));
        }
    }
    
    public BulkWriteResult getResult() {
        this.throwOnError();
        return this.createResult();
    }
    
    public boolean shouldStopSendingMoreBatches() {
        return this.ordered && this.hasWriteErrors();
    }
    
    private void throwOnError() {
        if (!this.writeErrors.isEmpty() || !this.writeConcernErrors.isEmpty()) {
            throw new BulkWriteException(this.createResult(), new ArrayList<BulkWriteError>(this.writeErrors), this.writeConcernErrors.isEmpty() ? null : this.writeConcernErrors.get(this.writeConcernErrors.size() - 1), this.serverAddress);
        }
    }
    
    private BulkWriteResult createResult() {
        return this.writeConcern.callGetLastError() ? new AcknowledgedBulkWriteResult(this.insertedCount, this.matchedCount, this.removedCount, this.modifiedCount, new ArrayList<BulkWriteUpsert>(this.writeUpserts)) : new UnacknowledgedBulkWriteResult();
    }
    
    private boolean hasWriteErrors() {
        return !this.writeErrors.isEmpty();
    }
}
