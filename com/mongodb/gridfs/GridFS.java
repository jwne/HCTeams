package com.mongodb.gridfs;

import java.util.logging.*;
import com.mongodb.*;
import org.bson.types.*;
import java.util.*;
import java.io.*;

public class GridFS
{
    private static final Logger LOGGER;
    public static final int DEFAULT_CHUNKSIZE = 261120;
    @Deprecated
    public static final long MAX_CHUNKSIZE = 3500000L;
    public static final String DEFAULT_BUCKET = "fs";
    @Deprecated
    protected final DB _db;
    @Deprecated
    protected final String _bucketName;
    @Deprecated
    protected final DBCollection _filesCollection;
    @Deprecated
    protected final DBCollection _chunkCollection;
    
    public GridFS(final DB db) {
        this(db, "fs");
    }
    
    public GridFS(final DB db, final String bucket) {
        super();
        this._db = db;
        this._bucketName = bucket;
        this._filesCollection = this._db.getCollection(this._bucketName + ".files");
        this._chunkCollection = this._db.getCollection(this._bucketName + ".chunks");
        try {
            if (this._filesCollection.count() < 1000L) {
                this._filesCollection.ensureIndex(BasicDBObjectBuilder.start().add("filename", 1).add("uploadDate", 1).get());
            }
            if (this._chunkCollection.count() < 1000L) {
                this._chunkCollection.ensureIndex(BasicDBObjectBuilder.start().add("files_id", 1).add("n", 1).get(), BasicDBObjectBuilder.start().add("unique", true).get());
            }
        }
        catch (MongoException e) {
            GridFS.LOGGER.info(String.format("Unable to ensure indices on GridFS collections in database %s", db.getName()));
        }
        this._filesCollection.setObjectClass(GridFSDBFile.class);
    }
    
    public DBCursor getFileList() {
        return this.getFileList(new BasicDBObject());
    }
    
    public DBCursor getFileList(final DBObject query) {
        return this.getFileList(query, new BasicDBObject("filename", 1));
    }
    
    public DBCursor getFileList(final DBObject query, final DBObject sort) {
        return this._filesCollection.find(query).sort(sort);
    }
    
    public GridFSDBFile find(final ObjectId objectId) {
        return this.findOne(objectId);
    }
    
    public GridFSDBFile findOne(final ObjectId objectId) {
        return this.findOne(new BasicDBObject("_id", objectId));
    }
    
    public GridFSDBFile findOne(final String filename) {
        return this.findOne(new BasicDBObject("filename", filename));
    }
    
    public GridFSDBFile findOne(final DBObject query) {
        return this._fix(this._filesCollection.findOne(query));
    }
    
    public List<GridFSDBFile> find(final String filename) {
        return this.find(filename, null);
    }
    
    public List<GridFSDBFile> find(final String filename, final DBObject sort) {
        return this.find(new BasicDBObject("filename", filename), sort);
    }
    
    public List<GridFSDBFile> find(final DBObject query) {
        return this.find(query, null);
    }
    
    public List<GridFSDBFile> find(final DBObject query, final DBObject sort) {
        final List<GridFSDBFile> files = new ArrayList<GridFSDBFile>();
        DBCursor c = null;
        try {
            c = this._filesCollection.find(query);
            if (sort != null) {
                c.sort(sort);
            }
            while (c.hasNext()) {
                files.add(this._fix(c.next()));
            }
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
        return files;
    }
    
    @Deprecated
    protected GridFSDBFile _fix(final Object o) {
        if (o == null) {
            return null;
        }
        if (!(o instanceof GridFSDBFile)) {
            throw new RuntimeException("somehow didn't get a GridFSDBFile");
        }
        final GridFSDBFile f = (GridFSDBFile)o;
        f._fs = this;
        return f;
    }
    
    public void remove(final ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("file id can not be null");
        }
        this._filesCollection.remove(new BasicDBObject("_id", id));
        this._chunkCollection.remove(new BasicDBObject("files_id", id));
    }
    
    public void remove(final String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename can not be null");
        }
        this.remove(new BasicDBObject("filename", filename));
    }
    
    public void remove(final DBObject query) {
        if (query == null) {
            throw new IllegalArgumentException("query can not be null");
        }
        for (final GridFSDBFile f : this.find(query)) {
            f.remove();
        }
    }
    
    public GridFSInputFile createFile(final byte[] data) {
        return this.createFile(new ByteArrayInputStream(data), true);
    }
    
    public GridFSInputFile createFile(final File file) throws IOException {
        return this.createFile(new FileInputStream(file), file.getName(), true);
    }
    
    public GridFSInputFile createFile(final InputStream in) {
        return this.createFile(in, null);
    }
    
    public GridFSInputFile createFile(final InputStream in, final boolean closeStreamOnPersist) {
        return this.createFile(in, null, closeStreamOnPersist);
    }
    
    public GridFSInputFile createFile(final InputStream in, final String filename) {
        return new GridFSInputFile(this, in, filename);
    }
    
    public GridFSInputFile createFile(final InputStream in, final String filename, final boolean closeStreamOnPersist) {
        return new GridFSInputFile(this, in, filename, closeStreamOnPersist);
    }
    
    public GridFSInputFile createFile(final String filename) {
        return new GridFSInputFile(this, filename);
    }
    
    public GridFSInputFile createFile() {
        return new GridFSInputFile(this);
    }
    
    public String getBucketName() {
        return this._bucketName;
    }
    
    public DB getDB() {
        return this._db;
    }
    
    protected DBCollection getFilesCollection() {
        return this._filesCollection;
    }
    
    protected DBCollection getChunksCollection() {
        return this._chunkCollection;
    }
    
    static {
        LOGGER = Logger.getLogger("com.mongodb.gridfs");
    }
}
