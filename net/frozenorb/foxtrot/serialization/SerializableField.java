package net.frozenorb.foxtrot.serialization;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface SerializableField {
    Class<?> serializer() default Object.class;
    
    String name() default "${ACTUAL_FIELD_NAME}";
}
