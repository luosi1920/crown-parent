package com.yibai.crown.base.logstash;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 2018/04/04.
 */
public class UserIpConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        if (event instanceof LoggingMessageEvent) {
            return ((LoggingMessageEvent) event).getUserIp();
        }
        return "";
    }

}
