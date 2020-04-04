package com.sn.theft.annotation;

import java.lang.annotation.*;

/**
 * @author: songning
 * @date: 2020/2/20 22:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AClassConvert {
    boolean ignore() default false;
    String fieldName() default "";
}
