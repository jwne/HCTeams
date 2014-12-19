package com.mongodb.gridfs;

import java.io.*;
import com.mongodb.*;

public class GridFSDBFile extends GridFSFile
{
    public InputStream getInputStream() {
        return new MyInputStream();
    }
    
    public long writeTo(final String filename) throws IOException {
        return this.writeTo(new File(filename));
    }
    
    public long writeTo(final File f) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            return this.writeTo(out);
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    public long writeTo(final OutputStream out) throws IOException {
        for (int nc = this.numChunks(), i = 0; i < nc; ++i) {
            out.write(this.getChunk(i));
        }
        return this._length;
    }
    
    byte[] getChunk(final int i) {
        if (this._fs == null) {
            throw new RuntimeException("no gridfs!");
        }
        final DBObject chunk = this._fs._chunkCollection.findOne(BasicDBObjectBuilder.start("files_id", this._id).add("n", i).get());
        if (chunk == null) {
            throw new MongoException("can't find a chunk!  file id: " + this._id + " chunk: " + i);
        }
        return (byte[])chunk.get("data");
    }
    
    void remove() {
        this._fs._filesCollection.remove(new BasicDBObject("_id", this._id));
        this._fs._chunkCollection.remove(new BasicDBObject("files_id", this._id));
    }
    
    class MyInputStream extends InputStream
    {
        final int _numChunks;
        int _currentChunkIdx;
        int _offset;
        byte[] _data;
        
        MyInputStream() {
            super();
            this._currentChunkIdx = -1;
            this._offset = 0;
            this._data = null;
            this._numChunks = GridFSDBFile.this.numChunks();
        }
        
        public int available() {
            if (this._data == null) {
                return 0;
            }
            return this._data.length - this._offset;
        }
        
        public void close() {
        }
        
        public void mark(final int readlimit) {
            throw new RuntimeException("mark not supported");
        }
        
        public void reset() {
            throw new RuntimeException("mark not supported");
        }
        
        public boolean markSupported() {
            return false;
        }
        
        public int read() {
            final byte[] b = { 0 };
            final int res = this.read(b);
            if (res < 0) {
                return -1;
            }
            return b[0] & 0xFF;
        }
        
        public int read(final byte[] b) {
            return this.read(b, 0, b.length);
        }
        
        public int read(final byte[] b, final int off, final int len) {
            if (this._data == null || this._offset >= this._data.length) {
                if (this._currentChunkIdx + 1 >= this._numChunks) {
                    return -1;
                }
                this._data = GridFSDBFile.this.getChunk(++this._currentChunkIdx);
                this._offset = 0;
            }
            final int r = Math.min(len, this._data.length - this._offset);
            System.arraycopy(this._data, this._offset, b, off, r);
            this._offset += r;
            return r;
        }
        
        public long skip(final long numBytesToSkip) throws IOException {
            if (numBytesToSkip <= 0L) {
                return 0L;
            }
            if (this._currentChunkIdx == this._numChunks) {
                return 0L;
            }
            long offsetInFile = 0L;
            if (this._currentChunkIdx >= 0) {
                offsetInFile = this._currentChunkIdx * GridFSDBFile.this._chunkSize + this._offset;
            }
            if (numBytesToSkip + offsetInFile >= GridFSDBFile.this._length) {
                this._currentChunkIdx = this._numChunks;
                this._data = null;
                return GridFSDBFile.this._length - offsetInFile;
            }
            final int temp = this._currentChunkIdx;
            this._currentChunkIdx = (int)((numBytesToSkip + offsetInFile) / GridFSDBFile.this._chunkSize);
            if (temp != this._currentChunkIdx) {
                this._data = GridFSDBFile.this.getChunk(this._currentChunkIdx);
            }
            this._offset = (int)((numBytesToSkip + offsetInFile) % GridFSDBFile.this._chunkSize);
            return numBytesToSkip;
        }
    }
}
