package com.mongodb;

import java.util.logging.*;
import org.bson.util.*;

final class Loggers
{
    public static final String PREFIX = "com.mongodb.driver";
    
    public static Logger getLogger(final String suffix) {
        Assertions.notNull("suffix", suffix);
        if (suffix.startsWith(".") || suffix.endsWith(".")) {
            throw new IllegalArgumentException("The suffix can not start or end with a '.'");
        }
        return Logger.getLogger("com.mongodb.driver." + suffix);
    }
}
