package json_schema_validation_example.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidateWithSchema {
    String name();
}
