package com.yibai.crown.base.annotation;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 必须指明激活的profile，配合{@link org.springframework.context.annotation.Profile}使用
 * 2017/12/4.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ProfileSpecifiedCondition.class)
public @interface ProfileSpecified {

}
