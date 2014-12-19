package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.*;
import java.lang.reflect.*;

class PoolImplUtils
{
    static Class<?> getFactoryType(final Class<? extends PooledObjectFactory> factory) {
        return (Class<?>)getGenericType(PooledObjectFactory.class, factory);
    }
    
    private static <T> Object getGenericType(final Class<T> type, final Class<? extends T> clazz) {
        final Type[] arr$;
        final Type[] interfaces = arr$ = clazz.getGenericInterfaces();
        for (final Type iface : arr$) {
            if (iface instanceof ParameterizedType) {
                final ParameterizedType pi = (ParameterizedType)iface;
                if (pi.getRawType() instanceof Class && type.isAssignableFrom((Class<?>)pi.getRawType())) {
                    return getTypeParameter(clazz, pi.getActualTypeArguments()[0]);
                }
            }
        }
        final Class<? extends T> superClazz = (Class<? extends T>)clazz.getSuperclass();
        final Object result = getGenericType((Class<Object>)type, superClazz);
        if (result instanceof Class) {
            return result;
        }
        if (result instanceof Integer) {
            final ParameterizedType superClassType = (ParameterizedType)clazz.getGenericSuperclass();
            return getTypeParameter(clazz, superClassType.getActualTypeArguments()[(int)result]);
        }
        return null;
    }
    
    private static Object getTypeParameter(final Class<?> clazz, final Type argType) {
        if (argType instanceof Class) {
            return argType;
        }
        final TypeVariable<?>[] tvs = clazz.getTypeParameters();
        for (int i = 0; i < tvs.length; ++i) {
            if (tvs[i].equals(argType)) {
                return i;
            }
        }
        return null;
    }
}
