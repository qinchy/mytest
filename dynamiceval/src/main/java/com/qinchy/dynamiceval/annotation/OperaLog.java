package com.qinchy.dynamiceval.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义注解实现日志保存。ip是使用SPEL表达式绑定动态变量参数值的形式传递，要求ip参数存在并赋值
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface OperaLog {
    String message() default "";

    String ip() default "127.0.0.1";
}
