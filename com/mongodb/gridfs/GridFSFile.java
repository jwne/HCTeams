package com.mongodb.gridfs;

import com.mongodb.*;
import org.bson.*;
import com.mongodb.util.*;
import java.util.*;

public abstract class GridFSFile implements DBObject
{
    @Deprecated
    protected GridFS _fs;
    Object _id;
    String _filename;
    String _contentType;
    long _length;
    long _chunkSize;
    Date _uploadDate;
    List<String> _aliases;
    DBObject _extradata;
    String _md5;
    static final Set<String> VALID_FIELDS;
    
    public GridFSFile() {
        super();
        this._fs = null;
        this._extradata = new BasicDBObject();
    }
    
    public void save() {
        if (this._fs == null) {
            throw new MongoException("need _fs");
        }
        this._fs._filesCollection.save(this);
    }
    
    public void validate() {
        if (this._fs == null) {
            throw new MongoException("no _fs");
        }
        if (this._md5 == null) {
            throw new MongoException("no _md5 stored");
        }
        final DBObject cmd = new BasicDBObject("filemd5", this._id);
        cmd.put("root", this._fs._bucketName);
        final DBObject res = this._fs._db.command(cmd);
        if (res == null || !res.containsField("md5")) {
            throw new MongoException("no md5 returned from server: " + res);
        }
        final String m = res.get("md5").toString();
        if (m.equals(this._md5)) {
            return;
        }
        throw new MongoException("md5 differ.  mine [" + this._md5 + "] theirs [" + m + "]");
    }
    
    public int numChunks() {
        double d = this._length;
        d /= this._chunkSize;
        return (int)Math.ceil(d);
    }
    
    public Object getId() {
        return this._id;
    }
    
    public String getFilename() {
        return this._filename;
    }
    
    public String getContentType() {
        return this._contentType;
    }
    
    public long getLength() {
        return this._length;
    }
    
    public long getChunkSize() {
        return this._chunkSize;
    }
    
    public Date getUploadDate() {
        return this._uploadDate;
    }
    
    public List<String> getAliases() {
        return (List<String>)this._extradata.get("aliases");
    }
    
    public DBObject getMetaData() {
        return (DBObject)this._extradata.get("metadata");
    }
    
    public void setMetaData(final DBObject metadata) {
        this._extradata.put("metadata", metadata);
    }
    
    public String getMD5() {
        return this._md5;
    }
    
    public Object put(final String key, final Object v) {
        if (key == null) {
            throw new RuntimeException("key should never be null");
        }
        if (key.equals("_id")) {
            this._id = v;
        }
        else if (key.equals("filename")) {
            this._filename = ((v == null) ? null : v.toString());
        }
        else if (key.equals("contentType")) {
            this._contentType = (String)v;
        }
        else if (key.equals("length")) {
            this._length = ((Number)v).longValue();
        }
        else if (key.equals("chunkSize")) {
            this._chunkSize = ((Number)v).longValue();
        }
        else if (key.equals("uploadDate")) {
            this._uploadDate = (Date)v;
        }
        else if (key.equals("md5")) {
            this._md5 = (String)v;
        }
        else {
            this._extradata.put(key, v);
        }
        return v;
    }
    
    public Object get(final String key) {
        if (key == null) {
            throw new RuntimeException("key should never be null");
        }
        if (key.equals("_id")) {
            return this._id;
        }
        if (key.equals("filename")) {
            return this._filename;
        }
        if (key.equals("contentType")) {
            return this._contentType;
        }
        if (key.equals("length")) {
            return this._length;
        }
        if (key.equals("chunkSize")) {
            return this._chunkSize;
        }
        if (key.equals("uploadDate")) {
            return this._uploadDate;
        }
        if (key.equals("md5")) {
            return this._md5;
        }
        return this._extradata.get(key);
    }
    
    public void putAll(final BSONObject o) {
        throw new UnsupportedOperationException();
    }
    
    public void putAll(final Map m) {
        throw new UnsupportedOperationException();
    }
    
    public Map toMap() {
        throw new UnsupportedOperationException();
    }
    
    public Object removeField(final String key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public boolean containsKey(final String s) {
        return this.containsField(s);
    }
    
    public boolean containsField(final String s) {
        return this.keySet().contains(s);
    }
    
    public Set<String> keySet() {
        final Set<String> keys = new HashSet<String>();
        keys.addAll(GridFSFile.VALID_FIELDS);
        keys.addAll(this._extradata.keySet());
        return keys;
    }
    
    public boolean isPartialObject() {
        return false;
    }
    
    public void markAsPartialObject() {
        throw new RuntimeException("can't load partial GridFSFile file");
    }
    
    public String toString() {
        return JSON.serialize(this);
    }
    
    protected GridFS getGridFS() {
        return this._fs;
    }
    
    protected void setGridFS(final GridFS fs) {
        this._fs = fs;
    }
    
    static {
        VALID_FIELDS = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList("_id", "filename", "contentType", "length", "chunkSize", "uploadDate", "aliases", "md5")));
    }
}
