package com.snicesoft.viewbind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhu zhe
 * @version V1.0
 * @since 2015年4月15日 上午9:52:28
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBind {
    public int value() default 0;

    public String name() default "";

    public DataType dataType() default DataType.STRING;

    public int loadingResId() default 0;

    public String loadingResName() default "";

    public int failResId() default 0;

    public String failResName() default "";

    /**
     * 前缀
     *
     * @return
     */
    public String prefix() default "";

    /**
     * 后缀
     *
     * @return
     */
    public String suffix() default "";

    /**
     * 格式化Date
     *
     * @return
     */
    public String pattern() default "";

}
