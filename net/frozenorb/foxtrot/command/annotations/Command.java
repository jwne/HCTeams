package net.frozenorb.foxtrot.command.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String[] names();
    
    String[] flags() default { "" };
    
    String permissionNode();
}
