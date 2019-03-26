package com.yibai.crown.base.logstash;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.composite.loggingevent.LoggingEventPatternJsonProvider;

import java.io.IOException;

/**
 * 2017/10/18.
 */
public class StashLoggingEventPatternJsonProvider extends LoggingEventPatternJsonProvider {

    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent iLoggingEvent) throws IOException {
        if (iLoggingEvent instanceof LoggingMessageEvent) {
            super.writeTo(generator, iLoggingEvent);
        }
    }

}
