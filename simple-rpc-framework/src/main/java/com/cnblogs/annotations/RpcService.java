package com.cnblogs.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService {
    /**
     * 接口版本，用于区分不同的service实现
     * @return
     */
    String version() default "";

    /**
     * 用于区分不同组的同一个接口
     * @return
     */
    String group() default "";
}
