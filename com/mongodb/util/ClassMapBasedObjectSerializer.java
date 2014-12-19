package com.mongodb.util;

import org.bson.util.*;
import org.bson.*;
import java.util.*;

class ClassMapBasedObjectSerializer extends AbstractObjectSerializer
{
    private ClassMap<ObjectSerializer> _serializers;
    
    ClassMapBasedObjectSerializer() {
        super();
        this._serializers = new ClassMap<ObjectSerializer>();
    }
    
    void addObjectSerializer(final Class c, final ObjectSerializer serializer) {
        this._serializers.put(c, serializer);
    }
    
    public void serialize(Object obj, final StringBuilder buf) {
        obj = BSON.applyEncodingHooks(obj);
        if (obj == null) {
            buf.append(" null ");
            return;
        }
        ObjectSerializer serializer = null;
        final List<Class<?>> ancestors = ClassMap.getAncestry(obj.getClass());
        for (final Class<?> ancestor : ancestors) {
            serializer = this._serializers.get(ancestor);
            if (serializer != null) {
                break;
            }
        }
        if (serializer == null && obj.getClass().isArray()) {
            serializer = this._serializers.get(Object[].class);
        }
        if (serializer == null) {
            throw new RuntimeException("json can't serialize type : " + obj.getClass());
        }
        serializer.serialize(obj, buf);
    }
}
