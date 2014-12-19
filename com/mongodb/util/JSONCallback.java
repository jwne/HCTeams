package com.mongodb.util;

import java.text.*;
import org.bson.*;
import java.util.regex.*;
import com.mongodb.*;
import java.util.*;
import org.bson.types.*;

public class JSONCallback extends BasicBSONCallback
{
    private boolean _lastArray;
    public static final String _msDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String _secDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
    public JSONCallback() {
        super();
        this._lastArray = false;
    }
    
    public BSONObject create() {
        return new BasicDBObject();
    }
    
    protected BSONObject createList() {
        return new BasicDBList();
    }
    
    @Deprecated
    public void objectStart(final boolean array, final String name) {
        super.objectStart(this._lastArray = array, name);
    }
    
    public Object objectDone() {
        final String name = this.curName();
        Object o = super.objectDone();
        if (this._lastArray) {
            return o;
        }
        final BSONObject b = (BSONObject)o;
        if (b.containsField("$oid")) {
            o = new ObjectId((String)b.get("$oid"));
        }
        else if (b.containsField("$date")) {
            if (b.get("$date") instanceof Number) {
                o = new Date(((Number)b.get("$date")).longValue());
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                o = format.parse(b.get("$date").toString(), new ParsePosition(0));
                if (o == null) {
                    format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                    o = format.parse(b.get("$date").toString(), new ParsePosition(0));
                }
            }
        }
        else if (b.containsField("$regex")) {
            o = Pattern.compile((String)b.get("$regex"), BSON.regexFlags((String)b.get("$options")));
        }
        else if (b.containsField("$ts")) {
            final Integer ts = ((Number)b.get("$ts")).intValue();
            final Integer inc = ((Number)b.get("$inc")).intValue();
            o = new BSONTimestamp(ts, inc);
        }
        else if (b.containsField("$timestamp")) {
            final BSONObject tsObject = (BSONObject)b.get("$timestamp");
            final Integer ts2 = ((Number)tsObject.get("t")).intValue();
            final Integer inc2 = ((Number)tsObject.get("i")).intValue();
            o = new BSONTimestamp(ts2, inc2);
        }
        else if (b.containsField("$code")) {
            if (b.containsField("$scope")) {
                o = new CodeWScope((String)b.get("$code"), (BSONObject)b.get("$scope"));
            }
            else {
                o = new Code((String)b.get("$code"));
            }
        }
        else if (b.containsField("$ref")) {
            o = new DBRef(null, (String)b.get("$ref"), b.get("$id"));
        }
        else if (b.containsField("$minKey")) {
            o = new MinKey();
        }
        else if (b.containsField("$maxKey")) {
            o = new MaxKey();
        }
        else if (b.containsField("$uuid")) {
            o = UUID.fromString((String)b.get("$uuid"));
        }
        else if (b.containsField("$binary")) {
            final int type = (int)b.get("$type");
            final byte[] bytes = new Base64Codec().decode((String)b.get("$binary"));
            o = new Binary((byte)type, bytes);
        }
        else if (b.containsField("$numberLong")) {
            o = Long.valueOf((String)b.get("$numberLong"));
        }
        if (!this.isStackEmpty()) {
            this._put(name, o);
        }
        else {
            o = (BSON.hasDecodeHooks() ? BSON.applyDecodingHooks(o) : o);
            this.setRoot(o);
        }
        return o;
    }
}
