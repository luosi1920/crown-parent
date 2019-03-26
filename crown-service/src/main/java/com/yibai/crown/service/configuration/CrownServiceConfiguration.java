package com.yibai.crown.service.configuration;


import com.yibai.crown.base.properties.AsyncHttpClientProperties;
import com.yibai.crown.service.component.SnowflakeIdWorker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
@EnableConfigurationProperties({AsyncHttpClientProperties.class})
public class CrownServiceConfiguration implements EnvironmentAware {

    private Environment environment;

//    @Autowired
//    private SnowflakeProperties snowflakeProperties;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "snowflake", name = "enabled", havingValue = "true", matchIfMissing = true)
    public SnowflakeIdWorker snowflakeIdWorker() {
        long worker = Long.parseLong(environment.getProperty("snowflake.worker"));
        long datacenter = Long.parseLong(environment.getProperty("snowflake.datacenter"));
        return new SnowflakeIdWorker(worker, datacenter);
    }

//    @Bean
//    public DubboProviderTraceHandler dubboProviderTraceHandler(Tracing tracing) {
//        return new DubboProviderTraceHandler(tracing);
//    }

}
