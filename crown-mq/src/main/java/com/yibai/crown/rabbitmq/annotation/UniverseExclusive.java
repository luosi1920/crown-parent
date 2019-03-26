package com.yibai.crown.rabbitmq.annotation;

import java.lang.annotation.*;

/**
 * MQ全局排他/独占消费端，应用多实例情况下排他注册MQ消费端，独占串行消费
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface UniverseExclusive {

}
