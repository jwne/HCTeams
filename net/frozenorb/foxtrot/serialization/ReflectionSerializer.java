package net.frozenorb.foxtrot.serialization;

import com.mongodb.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import com.mongodb.util.*;

public abstract class ReflectionSerializer implements Serializable<BasicDBObject>
{
    @Override
    public BasicDBObject serialize() {
        final BasicDBObject db = new BasicDBObject();
        try {
            this.serializeClass(this.getClass(), db);
            Class<?> c = this.getClass();
            final String name = c.getName();
            final boolean serializableClass = c.isAnnotationPresent(SerializableClass.class);
            final SerializableClass cs = c.getAnnotation(SerializableClass.class);
            if ((serializableClass && cs.serializeSuperclasses()) || !serializableClass) {
                int classesDone = 0;
                while (c.getSuperclass() != null) {
                    if (serializableClass && ++classesDone > cs.maxSuperclassDepth()) {
                        break;
                    }
                    c = c.getSuperclass();
                    this.serializeClass(c, db);
                }
            }
            if (serializableClass && cs.appendClassSignature()) {
                db.append("#className", name);
            }
        }
        catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return db;
    }
    
    @Override
    public void deserialize(final BasicDBObject dbobj) {
        try {
            final SerializableClass cs = this.getClass().getAnnotation(SerializableClass.class);
            this.deserializeClass(this.getClass(), dbobj);
            Class<?> c = this.getClass();
            final boolean serializableClass = c.isAnnotationPresent(SerializableClass.class);
            if ((serializableClass && cs.serializeSuperclasses()) || !serializableClass) {
                int classesDone = 0;
                while (c.getSuperclass() != null) {
                    if (serializableClass && ++classesDone > cs.maxSuperclassDepth()) {
                        break;
                    }
                    c = c.getSuperclass();
                    this.deserializeClass(c, dbobj);
                }
            }
        }
        catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }
    
    private void serializeClass(final Class<?> c, final BasicDBObject db) throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final boolean serializableClass = c.isAnnotationPresent(SerializableClass.class);
        for (final Field f : c.getDeclaredFields()) {
            f.setAccessible(true);
            if (!Modifier.isTransient(f.getModifiers())) {
                if (f.isAnnotationPresent(SerializableField.class) || serializableClass) {
                    final Object o = f.get(this);
                    if (o != null) {
                        final Class<?> type = f.getType();
                        if (JSONSerializable.class.isAssignableFrom(type)) {
                            final Method ser = type.getDeclaredMethod("serialize", (Class<?>[])new Class[0]);
                            final BasicDBObject ses = (BasicDBObject)ser.invoke(o, new Object[0]);
                            final SerializableField sf = f.getAnnotation(SerializableField.class);
                            db.append((sf != null) ? sf.name().replace("${ACTUAL_FIELD_NAME}", f.getName()) : f.getName(), ses.append("#className", type.getName()));
                        }
                        else {
                            final SerializableField sf2 = f.getAnnotation(SerializableField.class);
                            Object js = new BasicDBObject();
                            if (sf2 != null && sf2.serializer() != Object.class) {
                                js = ((JSONSerializer)sf2.serializer().newInstance()).serialize(o);
                                ((BasicDBObject)js).append("#serializingClass", sf2.serializer().getName());
                                ((BasicDBObject)js).append("#className", type.getName());
                            }
                            else {
                                js = o;
                            }
                            db.append((sf2 != null) ? sf2.name().replace("${ACTUAL_FIELD_NAME}", f.getName()) : f.getName(), js);
                        }
                    }
                }
            }
        }
    }
    
    private void deserializeClass(final Class<?> c, final BasicDBObject dbobj) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InstantiationException {
        final boolean serializableClass = c.isAnnotationPresent(SerializableClass.class);
        for (final Field f : c.getDeclaredFields()) {
            f.setAccessible(true);
            if (!Modifier.isTransient(f.getModifiers())) {
                if (f.isAnnotationPresent(SerializableField.class) || serializableClass) {
                    final SerializableField sf = f.getAnnotation(SerializableField.class);
                    if (dbobj.containsField((sf != null) ? sf.name().replace("${ACTUAL_FIELD_NAME}", f.getName()) : f.getName())) {
                        final Object json = dbobj.get((sf != null) ? sf.name().replace("${ACTUAL_FIELD_NAME}", f.getName()) : f.getName());
                        if (json instanceof BasicDBObject && ((BasicDBObject)json).containsField("#className")) {
                            final String className = ((BasicDBObject)json).getString("#className");
                            Class<?> deser = null;
                            if (((BasicDBObject)json).containsField("#serializingClass")) {
                                final String serializingClass = ((BasicDBObject)json).getString("#serializingClass");
                                deser = Class.forName(serializingClass);
                            }
                            final Class<?> cls = Class.forName(className);
                            for (final Constructor<?> css : cls.getConstructors()) {
                                css.setAccessible(true);
                            }
                            Object set = null;
                            if (JSONSerializable.class.isAssignableFrom(cls)) {
                                set = (JSONSerializable)cls.newInstance();
                            }
                            if (deser == null) {
                                ((JSONSerializable)set).deserialize((BasicDBObject)json);
                            }
                            else {
                                set = deser.newInstance();
                                set = ((JSONSerializer)set).deserialize((BasicDBObject)json);
                            }
                            final Field modifiersField = Field.class.getDeclaredField("modifiers");
                            modifiersField.setAccessible(true);
                            modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
                            f.set(this, set);
                        }
                        else {
                            final Field modifiersField2 = Field.class.getDeclaredField("modifiers");
                            modifiersField2.setAccessible(true);
                            modifiersField2.setInt(f, f.getModifiers() & 0xFFFFFFEF);
                            f.set(this, json);
                        }
                    }
                }
            }
        }
    }
    
    public boolean isValidJSON(final String json) {
        try {
            JSON.parse(json);
            return true;
        }
        catch (JSONParseException e) {
            return false;
        }
    }
}
