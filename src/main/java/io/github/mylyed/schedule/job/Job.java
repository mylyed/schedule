package io.github.mylyed.schedule.job;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by lilei on 2017/7/10.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Job {
    String value() default "";
}
