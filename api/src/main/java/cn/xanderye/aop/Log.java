package cn.xanderye.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/25 18:48:17
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String moduleName() default "";

    String methodName() default "";

    boolean logResult() default true;
}
