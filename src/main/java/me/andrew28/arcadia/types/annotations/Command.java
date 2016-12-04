package me.andrew28.arcadia.types.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Andrew Tran on 12/3/2016
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String command();
    String usage();
    String description();
    boolean caseSensitive() default false;
    boolean usePrefix() default true;
}
