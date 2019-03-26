package com.yibai.crown.base.trace;

import com.yibai.crown.base.logstash.StashMessageWriter;
import com.yibai.crown.base.logstash.TraceMessageEvent;
import com.yibai.crown.base.properties.LoggingProperties;
import zipkin2.Call;
import zipkin2.codec.Encoding;
import zipkin2.reporter.Sender;

import java.util.List;

/**
 * 2017/10/17.
 */
public class StashTraceSender extends Sender {

    private final StashMessageWriter writer;
    private final LoggingProperties.Trace trace;

    public StashTraceSender(StashMessageWriter writer, LoggingProperties.Trace trace) {
        this.writer = writer;
        this.trace = trace;
    }

    @Override
    public Encoding encoding() {
        return Encoding.JSON;
    }

    @Override
    public int messageMaxBytes() {
        return 5 * 1024 * 1024;
    }

    @Override
    public int messageSizeInBytes(List<byte[]> encodedSpans) {
        return encoding().listSizeInBytes(encodedSpans);
    }

    @Override
    public Call<Void> sendSpans(List<byte[]> encodedSpans) {
        if (!trace.isEnabled()) {
            return Call.create(null);
        }
        String applicationName = System.getProperty("spring.application.name", "unknown-application");
        for (byte[] encodedSpan : encodedSpans) {
            TraceMessageEvent event = new TraceMessageEvent(applicationName, System.currentTimeMillis(), new String(encodedSpan));
            writer.write(event);
        }
        return Call.create(null);
    }

}
