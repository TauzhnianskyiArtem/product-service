package com.iprody.product.service.logging.aspect;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The AspectLogging annotation is used to annotate methods that should be logged by the logging aspect.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessOpsLogging {

    /**
     * The name of the domain on which the operations are performed.
     * @return String
     */
    String domain() default "";

    /**
     * Level specifies the log level at which the method should be logged.
     * @return LogLevel
     */
    Level level() default Level.INFO;
}

