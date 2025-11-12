package github.javaguide.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    String version() default "";

    String group() default "";
}
