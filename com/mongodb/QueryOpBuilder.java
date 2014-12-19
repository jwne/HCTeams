package com.mongodb;

class QueryOpBuilder
{
    static final String READ_PREFERENCE_META_OPERATOR = "$readPreference";
    private DBObject query;
    private DBObject orderBy;
    private DBObject hintObj;
    private String hintStr;
    private boolean explain;
    private boolean snapshot;
    private long maxTimeMS;
    private ReadPreference readPref;
    private DBObject specialFields;
    
    public QueryOpBuilder addQuery(final DBObject query) {
        this.query = query;
        return this;
    }
    
    public QueryOpBuilder addOrderBy(final DBObject orderBy) {
        this.orderBy = orderBy;
        return this;
    }
    
    public QueryOpBuilder addHint(final String hint) {
        this.hintStr = hint;
        return this;
    }
    
    public QueryOpBuilder addHint(final DBObject hint) {
        this.hintObj = hint;
        return this;
    }
    
    public QueryOpBuilder addSpecialFields(final DBObject specialFields) {
        this.specialFields = specialFields;
        return this;
    }
    
    public QueryOpBuilder addExplain(final boolean explain) {
        this.explain = explain;
        return this;
    }
    
    public QueryOpBuilder addSnapshot(final boolean snapshot) {
        this.snapshot = snapshot;
        return this;
    }
    
    public QueryOpBuilder addReadPreference(final ReadPreference readPref) {
        this.readPref = readPref;
        return this;
    }
    
    public QueryOpBuilder addMaxTimeMS(final long maxTimeMS) {
        this.maxTimeMS = maxTimeMS;
        return this;
    }
    
    public DBObject get() {
        DBObject lclQuery = this.query;
        if (lclQuery == null) {
            lclQuery = new BasicDBObject();
        }
        if (this.hasSpecialQueryFields()) {
            final DBObject queryop = (this.specialFields == null) ? new BasicDBObject() : this.specialFields;
            this.addToQueryObject(queryop, "$query", lclQuery, true);
            this.addToQueryObject(queryop, "$orderby", this.orderBy, false);
            if (this.hintStr != null) {
                this.addToQueryObject(queryop, "$hint", this.hintStr);
            }
            if (this.hintObj != null) {
                this.addToQueryObject(queryop, "$hint", this.hintObj);
            }
            if (this.explain) {
                queryop.put("$explain", true);
            }
            if (this.snapshot) {
                queryop.put("$snapshot", true);
            }
            if (this.readPref != null && this.readPref != ReadPreference.primary()) {
                queryop.put("$readPreference", this.readPref.toDBObject());
            }
            if (this.maxTimeMS > 0L) {
                queryop.put("$maxTimeMS", this.maxTimeMS);
            }
            return queryop;
        }
        return lclQuery;
    }
    
    private boolean hasSpecialQueryFields() {
        return this.readPref != null || this.specialFields != null || (this.orderBy != null && this.orderBy.keySet().size() > 0) || (this.hintStr != null || this.hintObj != null || this.snapshot || this.explain) || this.maxTimeMS > 0L;
    }
    
    private void addToQueryObject(final DBObject dbobj, final String field, final DBObject obj, final boolean sendEmpty) {
        if (obj == null) {
            return;
        }
        if (!sendEmpty && obj.keySet().size() == 0) {
            return;
        }
        this.addToQueryObject(dbobj, field, obj);
    }
    
    private void addToQueryObject(final DBObject dbobj, final String field, final Object obj) {
        if (obj == null) {
            return;
        }
        dbobj.put(field, obj);
    }
}
