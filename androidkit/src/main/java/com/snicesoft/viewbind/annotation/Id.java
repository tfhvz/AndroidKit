package com.snicesoft.viewbind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhu zhe
 * @version V1.0
 * @since 2015年4月15日 上午10:17:13
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    public int value() default 0;

    public String name() default "";
}