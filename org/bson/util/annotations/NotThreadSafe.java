package org.bson.util.annotations;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface NotThreadSafe {
}
