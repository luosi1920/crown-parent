package com.yibai.crown.base.logstash;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 2017/9/12.
 */
public class TraceIdConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        if (event instanceof LoggingMessageEvent) {
            return ((LoggingMessageEvent) event).getTraceId();
        } else if (event instanceof TraceMessageEvent) {
            return ((TraceMessageEvent) event).getTraceId();
        }
        return "";
    }
}
