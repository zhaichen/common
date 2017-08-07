package io.github.zhaichen.annotation;

import java.lang.annotation.*;

/**
 * Created with com.ptmind.datadeck.app.user.annotation.
 * Author: ZhaiChen
 * Date: 2017/8/3 17:12
 * Description:
 */



@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnLabel {
    String value() default "";
}
