package com.yibai.crown.provider.exception;


import com.google.common.base.Joiner;
import com.yibai.crown.base.component.ReloadableMessageSource;
import com.yibai.crown.base.helper.ApplicationContextHelper;
import org.apache.commons.lang3.StringUtils;


public class BizExceptionMessageHandler {
    private static ReloadableMessageSource messageSource;

    public static String getMessage(String errorCode, Object... args) {
        if (messageSource == null) {
            messageSource = ApplicationContextHelper.getBean(ReloadableMessageSource.class);
        }
        String message = messageSource.getMessage(errorCode, args);
        if (StringUtils.isBlank(message)) {
            StringBuilder arg = new StringBuilder("");
            if (args != null) {
                for (Object ag : args) {
                    arg.append(ag.toString());
                }
            }
            message = Joiner.on(" ").join("☺", arg.toString());
        }
        return errorCode+","+message;
    }

}
