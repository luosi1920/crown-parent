package com.yibai.crown.base.logstash;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 2017/12/20.
 */
public class UserIdConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        if (event instanceof LoggingMessageEvent) {
            return ((LoggingMessageEvent) event).getUserId();
        }
        return "";
    }

}
