package com.sn.theft.annotation;

import java.lang.annotation.*;

/**
 * @author sn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AControllerAspect {
    String description() default "";
}
