package com.mongodb.gridfs;

import org.bson.types.*;
import java.util.*;
import java.security.*;
import java.io.*;
import com.mongodb.*;
import com.mongodb.util.*;

public class GridFSInputFile extends GridFSFile
{
    private final InputStream _in;
    private boolean _closeStreamOnPersist;
    private boolean _savedChunks;
    private byte[] _buffer;
    private int _currentChunkNumber;
    private int _currentBufferPosition;
    private long _totalBytes;
    private MessageDigest _messageDigester;
    private OutputStream _outputStream;
    
    protected GridFSInputFile(final GridFS fs, final InputStream in, final String filename, final boolean closeStreamOnPersist) {
        super();
        this._savedChunks = false;
        this._buffer = null;
        this._currentChunkNumber = 0;
        this._currentBufferPosition = 0;
        this._totalBytes = 0L;
        this._messageDigester = null;
        this._outputStream = null;
        this._fs = fs;
        this._in = in;
        this._filename = filename;
        this._closeStreamOnPersist = closeStreamOnPersist;
        this._id = new ObjectId();
        this._chunkSize = 261120L;
        this._uploadDate = new Date();
        try {
            this._messageDigester = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5!");
        }
        this._messageDigester.reset();
        this._buffer = new byte[(int)this._chunkSize];
    }
    
    protected GridFSInputFile(final GridFS fs, final InputStream in, final String filename) {
        this(fs, in, filename, false);
    }
    
    protected GridFSInputFile(final GridFS fs, final String filename) {
        this(fs, null, filename);
    }
    
    protected GridFSInputFile(final GridFS fs) {
        this(fs, null, null);
    }
    
    public void setId(final Object id) {
        this._id = id;
    }
    
    public void setFilename(final String fn) {
        this._filename = fn;
    }
    
    public void setContentType(final String ct) {
        this._contentType = ct;
    }
    
    public void setChunkSize(final long chunkSize) {
        if (this._outputStream != null || this._savedChunks) {
            return;
        }
        this._chunkSize = chunkSize;
        this._buffer = new byte[(int)this._chunkSize];
    }
    
    public void save() {
        this.save(this._chunkSize);
    }
    
    public void save(final long chunkSize) {
        if (this._outputStream != null) {
            throw new MongoException("cannot mix OutputStream and regular save()");
        }
        if (!this._savedChunks) {
            try {
                this.saveChunks(chunkSize);
            }
            catch (IOException ioe) {
                throw new MongoException("couldn't save chunks", ioe);
            }
        }
        super.save();
    }
    
    public int saveChunks() throws IOException {
        return this.saveChunks(this._chunkSize);
    }
    
    public int saveChunks(final long chunkSize) throws IOException {
        if (this._outputStream != null) {
            throw new MongoException("cannot mix OutputStream and regular save()");
        }
        if (this._savedChunks) {
            throw new MongoException("chunks already saved!");
        }
        if (chunkSize <= 0L) {
            throw new MongoException("chunkSize must be greater than zero");
        }
        if (this._chunkSize != chunkSize) {
            this._chunkSize = chunkSize;
            this._buffer = new byte[(int)this._chunkSize];
        }
        int bytesRead = 0;
        while (bytesRead >= 0) {
            this._currentBufferPosition = 0;
            bytesRead = this._readStream2Buffer();
            this._dumpBuffer(true);
        }
        this._finishData();
        return this._currentChunkNumber;
    }
    
    public OutputStream getOutputStream() {
        if (this._outputStream == null) {
            this._outputStream = new MyOutputStream();
        }
        return this._outputStream;
    }
    
    private void _dumpBuffer(final boolean writePartial) {
        if (this._currentBufferPosition < this._chunkSize && !writePartial) {
            return;
        }
        if (this._currentBufferPosition == 0) {
            return;
        }
        byte[] writeBuffer = this._buffer;
        if (this._currentBufferPosition != this._chunkSize) {
            writeBuffer = new byte[this._currentBufferPosition];
            System.arraycopy(this._buffer, 0, writeBuffer, 0, this._currentBufferPosition);
        }
        final DBObject chunk = this.createChunk(this._id, this._currentChunkNumber, writeBuffer);
        this._fs._chunkCollection.save(chunk);
        ++this._currentChunkNumber;
        this._totalBytes += writeBuffer.length;
        this._messageDigester.update(writeBuffer);
        this._currentBufferPosition = 0;
    }
    
    protected DBObject createChunk(final Object id, final int currentChunkNumber, final byte[] writeBuffer) {
        return BasicDBObjectBuilder.start().add("files_id", id).add("n", currentChunkNumber).add("data", writeBuffer).get();
    }
    
    private int _readStream2Buffer() throws IOException {
        int bytesRead = 0;
        while (this._currentBufferPosition < this._chunkSize && bytesRead >= 0) {
            bytesRead = this._in.read(this._buffer, this._currentBufferPosition, (int)this._chunkSize - this._currentBufferPosition);
            if (bytesRead > 0) {
                this._currentBufferPosition += bytesRead;
            }
            else {
                if (bytesRead == 0) {
                    throw new RuntimeException("i'm doing something wrong");
                }
                continue;
            }
        }
        return bytesRead;
    }
    
    private void _finishData() {
        if (!this._savedChunks) {
            this._md5 = Util.toHex(this._messageDigester.digest());
            this._messageDigester = null;
            this._length = this._totalBytes;
            this._savedChunks = true;
            try {
                if (this._in != null && this._closeStreamOnPersist) {
                    this._in.close();
                }
            }
            catch (IOException ex) {}
        }
    }
    
    class MyOutputStream extends OutputStream
    {
        public void write(final int b) throws IOException {
            final byte[] byteArray = { (byte)(b & 0xFF) };
            this.write(byteArray, 0, 1);
        }
        
        public void write(final byte[] b, final int off, final int len) throws IOException {
            int offset = off;
            int length = len;
            int toCopy = 0;
            while (length > 0) {
                toCopy = length;
                if (toCopy > GridFSInputFile.this._chunkSize - GridFSInputFile.this._currentBufferPosition) {
                    toCopy = (int)GridFSInputFile.this._chunkSize - GridFSInputFile.this._currentBufferPosition;
                }
                System.arraycopy(b, offset, GridFSInputFile.this._buffer, GridFSInputFile.this._currentBufferPosition, toCopy);
                GridFSInputFile.this._currentBufferPosition += toCopy;
                offset += toCopy;
                length -= toCopy;
                if (GridFSInputFile.this._currentBufferPosition == GridFSInputFile.this._chunkSize) {
                    GridFSInputFile.this._dumpBuffer(false);
                }
            }
        }
        
        public void close() {
            GridFSInputFile.this._dumpBuffer(true);
            GridFSInputFile.this._finishData();
            GridFSInputFile.this.save();
        }
    }
}
