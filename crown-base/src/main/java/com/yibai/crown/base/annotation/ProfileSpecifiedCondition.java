package com.yibai.crown.base.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 2017/12/4.
 */
public class ProfileSpecifiedCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        if (environment == null) {
            return false;
        }
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles != null && activeProfiles.length > 0;
    }

}
