package net.frozenorb.foxtrot.command.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String name();
    
    boolean wildcard() default false;
    
    String defaultValue() default "";
}
