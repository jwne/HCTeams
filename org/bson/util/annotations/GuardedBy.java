package org.bson.util.annotations;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface GuardedBy {
    String value();
}
