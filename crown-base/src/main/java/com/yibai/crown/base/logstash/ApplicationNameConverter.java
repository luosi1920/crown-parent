package com.yibai.crown.base.logstash;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 2017/12/20.
 */
public class ApplicationNameConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return System.getProperty("spring.application.name", "");
    }

}
