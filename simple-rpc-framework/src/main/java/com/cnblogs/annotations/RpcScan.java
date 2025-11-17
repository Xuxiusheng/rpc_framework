package com.cnblogs.annotations;

import com.cnblogs.spring.CustomRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解扫描器，不使用默认的ComponentScan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CustomRegistrar.class)
public @interface RpcScan {
    String[] basePackages() default {"com.cnblogs"};
}
