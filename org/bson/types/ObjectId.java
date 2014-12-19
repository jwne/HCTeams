package org.bson.types;

import java.io.*;
import java.util.concurrent.atomic.*;
import java.nio.*;
import java.net.*;
import java.util.logging.*;
import java.lang.management.*;
import java.util.*;

public class ObjectId implements Comparable<ObjectId>, Serializable
{
    private static final long serialVersionUID = -4415279469780082174L;
    static final Logger LOGGER;
    final int _time;
    final int _machine;
    final int _inc;
    boolean _new;
    private static AtomicInteger _nextInc;
    private static final int _genmachine;
    
    public static ObjectId get() {
        return new ObjectId();
    }
    
    public static ObjectId createFromLegacyFormat(final int time, final int machine, final int inc) {
        return new ObjectId(time, machine, inc);
    }
    
    public static boolean isValid(final String s) {
        if (s == null) {
            return false;
        }
        final int len = s.length();
        if (len != 24) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            final char c = s.charAt(i);
            if (c < '0' || c > '9') {
                if (c < 'a' || c > 'f') {
                    if (c < 'A' || c > 'F') {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    @Deprecated
    public static ObjectId massageToObjectId(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof ObjectId) {
            return (ObjectId)o;
        }
        if (o instanceof String) {
            final String s = o.toString();
            if (isValid(s)) {
                return new ObjectId(s);
            }
        }
        return null;
    }
    
    public ObjectId(final Date time) {
        this(time, ObjectId._genmachine, ObjectId._nextInc.getAndIncrement());
    }
    
    public ObjectId(final Date time, final int inc) {
        this(time, ObjectId._genmachine, inc);
    }
    
    public ObjectId(final Date time, final int machine, final int inc) {
        super();
        this._time = (int)(time.getTime() / 1000L);
        this._machine = machine;
        this._inc = inc;
        this._new = false;
    }
    
    public ObjectId(final String s) {
        this(s, false);
    }
    
    public ObjectId(String s, final boolean babble) {
        super();
        if (!isValid(s)) {
            throw new IllegalArgumentException("invalid ObjectId [" + s + "]");
        }
        if (babble) {
            s = babbleToMongod(s);
        }
        final byte[] b = new byte[12];
        for (int i = 0; i < b.length; ++i) {
            b[i] = (byte)Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        final ByteBuffer bb = ByteBuffer.wrap(b);
        this._time = bb.getInt();
        this._machine = bb.getInt();
        this._inc = bb.getInt();
        this._new = false;
    }
    
    public ObjectId(final byte[] b) {
        super();
        if (b.length != 12) {
            throw new IllegalArgumentException("need 12 bytes");
        }
        final ByteBuffer bb = ByteBuffer.wrap(b);
        this._time = bb.getInt();
        this._machine = bb.getInt();
        this._inc = bb.getInt();
        this._new = false;
    }
    
    public ObjectId(final int time, final int machine, final int inc) {
        super();
        this._time = time;
        this._machine = machine;
        this._inc = inc;
        this._new = false;
    }
    
    public ObjectId() {
        super();
        this._time = (int)(System.currentTimeMillis() / 1000L);
        this._machine = ObjectId._genmachine;
        this._inc = ObjectId._nextInc.getAndIncrement();
        this._new = true;
    }
    
    public int hashCode() {
        int x = this._time;
        x += this._machine * 111;
        x += this._inc * 17;
        return x;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        final ObjectId other = massageToObjectId(o);
        return other != null && this._time == other._time && this._machine == other._machine && this._inc == other._inc;
    }
    
    @Deprecated
    public String toStringBabble() {
        return babbleToMongod(this.toStringMongod());
    }
    
    public String toHexString() {
        final StringBuilder buf = new StringBuilder(24);
        for (final byte b : this.toByteArray()) {
            buf.append(String.format("%02x", b & 0xFF));
        }
        return buf.toString();
    }
    
    @Deprecated
    public String toStringMongod() {
        final byte[] b = this.toByteArray();
        final StringBuilder buf = new StringBuilder(24);
        for (int i = 0; i < b.length; ++i) {
            final int x = b[i] & 0xFF;
            final String s = Integer.toHexString(x);
            if (s.length() == 1) {
                buf.append("0");
            }
            buf.append(s);
        }
        return buf.toString();
    }
    
    public byte[] toByteArray() {
        final byte[] b = new byte[12];
        final ByteBuffer bb = ByteBuffer.wrap(b);
        bb.putInt(this._time);
        bb.putInt(this._machine);
        bb.putInt(this._inc);
        return b;
    }
    
    static String _pos(final String s, final int p) {
        return s.substring(p * 2, p * 2 + 2);
    }
    
    @Deprecated
    public static String babbleToMongod(final String b) {
        if (!isValid(b)) {
            throw new IllegalArgumentException("invalid object id: " + b);
        }
        final StringBuilder buf = new StringBuilder(24);
        for (int i = 7; i >= 0; --i) {
            buf.append(_pos(b, i));
        }
        for (int i = 11; i >= 8; --i) {
            buf.append(_pos(b, i));
        }
        return buf.toString();
    }
    
    public String toString() {
        return this.toStringMongod();
    }
    
    int _compareUnsigned(final int i, final int j) {
        long li = 4294967295L;
        li &= i;
        long lj = 4294967295L;
        lj &= j;
        final long diff = li - lj;
        if (diff < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        if (diff > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)diff;
    }
    
    public int compareTo(final ObjectId id) {
        if (id == null) {
            return -1;
        }
        int x = this._compareUnsigned(this._time, id._time);
        if (x != 0) {
            return x;
        }
        x = this._compareUnsigned(this._machine, id._machine);
        if (x != 0) {
            return x;
        }
        return this._compareUnsigned(this._inc, id._inc);
    }
    
    public int getTimestamp() {
        return this._time;
    }
    
    public Date getDate() {
        return new Date(this._time * 1000L);
    }
    
    @Deprecated
    public long getTime() {
        return this._time * 1000L;
    }
    
    @Deprecated
    public int getTimeSecond() {
        return this._time;
    }
    
    @Deprecated
    public int getInc() {
        return this._inc;
    }
    
    @Deprecated
    public int _time() {
        return this._time;
    }
    
    @Deprecated
    public int getMachine() {
        return this._machine;
    }
    
    @Deprecated
    public int _machine() {
        return this._machine;
    }
    
    @Deprecated
    public int _inc() {
        return this._inc;
    }
    
    @Deprecated
    public boolean isNew() {
        return this._new;
    }
    
    @Deprecated
    public void notNew() {
        this._new = false;
    }
    
    @Deprecated
    public static int getGenMachineId() {
        return ObjectId._genmachine;
    }
    
    public static int getCurrentCounter() {
        return ObjectId._nextInc.get();
    }
    
    @Deprecated
    public static int getCurrentInc() {
        return ObjectId._nextInc.get();
    }
    
    @Deprecated
    public static int _flip(final int x) {
        int z = 0;
        z |= (x << 24 & 0xFF000000);
        z |= (x << 8 & 0xFF0000);
        z |= (x >> 8 & 0xFF00);
        z |= (x >> 24 & 0xFF);
        return z;
    }
    
    static {
        LOGGER = Logger.getLogger("org.bson.ObjectId");
        ObjectId._nextInc = new AtomicInteger(new Random().nextInt());
        try {
            int machinePiece;
            try {
                final StringBuilder sb = new StringBuilder();
                final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                while (e.hasMoreElements()) {
                    final NetworkInterface ni = e.nextElement();
                    sb.append(ni.toString());
                }
                machinePiece = sb.toString().hashCode() << 16;
            }
            catch (Throwable e2) {
                ObjectId.LOGGER.log(Level.WARNING, e2.getMessage(), e2);
                machinePiece = new Random().nextInt() << 16;
            }
            ObjectId.LOGGER.fine("machine piece post: " + Integer.toHexString(machinePiece));
            int processId = new Random().nextInt();
            try {
                processId = ManagementFactory.getRuntimeMXBean().getName().hashCode();
            }
            catch (Throwable t) {}
            final ClassLoader loader = ObjectId.class.getClassLoader();
            final int loaderId = (loader != null) ? System.identityHashCode(loader) : 0;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Integer.toHexString(processId));
            sb2.append(Integer.toHexString(loaderId));
            final int processPiece = sb2.toString().hashCode() & 0xFFFF;
            ObjectId.LOGGER.fine("process piece: " + Integer.toHexString(processPiece));
            _genmachine = (machinePiece | processPiece);
            ObjectId.LOGGER.fine("machine : " + Integer.toHexString(ObjectId._genmachine));
        }
        catch (Exception e3) {
            throw new RuntimeException(e3);
        }
    }
}
