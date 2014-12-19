package net.frozenorb.foxtrot.serialization;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface SerializableClass {
    boolean serializeSuperclasses() default false;
    
    int maxSuperclassDepth() default 0;
    
    boolean appendClassSignature() default true;
}
