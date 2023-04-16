package com.kafeihu.task.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * date:  2022/6/13 17:27
 * task框架启动注解
 *
 * @author kroulzhang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TaskVersionTips.class, SimpleTaskConfiguration.class})
public @interface EnableTaskProcess {

}
