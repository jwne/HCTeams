package com.mongodb.util;

abstract class AbstractObjectSerializer implements ObjectSerializer
{
    public String serialize(final Object obj) {
        final StringBuilder builder = new StringBuilder();
        this.serialize(obj, builder);
        return builder.toString();
    }
}
