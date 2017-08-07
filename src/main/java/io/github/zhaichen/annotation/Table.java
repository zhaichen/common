package io.github.zhaichen.annotation;

import java.lang.annotation.*;

/**
 * Created with com.ptmind.datadeck.app.user.annotation.
 * Author: ZhaiChen
 * Date: 2017/8/3 17:23
 * Description:
 */

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String value() default "";
}
