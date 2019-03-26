package com.yibai.crown.rabbitmq.configuration;

import com.yibai.crown.rabbitmq.component.ConsistencyRabbitTemplate;
import com.yibai.crown.rabbitmq.handler.ConsistentMessageHandler;
import com.yibai.crown.rabbitmq.handler.DefaultConsistentMessageHandler;
import com.yibai.crown.rabbitmq.handler.DefaultMessageCompensateProcessor;
import com.yibai.crown.rabbitmq.handler.MessageCompensateProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean({DataSource.class, JdbcTemplate.class})
public class ConsistencyRabbitConfiguration extends CrownRabbitConfiguration {

    @Autowired
    private JdbcTemplate jdbcTemplate;
//    @Autowired
//    private ShardingDataSourceProperties shardingDataSourceProperties;

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "consistencyRabbitTemplate")
    public ConsistencyRabbitTemplate consistencyRabbitTemplate(ConnectionFactory connectionFactory) {
        ConsistentMessageHandler consistentMessageHandler = consistentMessageHandler();
        ConsistencyRabbitTemplate template = new ConsistencyRabbitTemplate(connectionFactory, consistentMessageHandler);
        initTemplate(template);
        template.setConfirmCallback(consistentMessageHandler::onConfirm);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public ConsistentMessageHandler consistentMessageHandler() {
        return new DefaultConsistentMessageHandler(jdbcTemplate);
    }

    @Configuration
//    @ConditionalOnBean(CuratorFramework.class)
    public static class MqMessageCompensateConfiguration {

        @Autowired
        private ConsistencyRabbitTemplate rabbitTemplate;
//        @Autowired
//        private CuratorFramework curatorFramework;
        @Autowired
        private ConsistentMessageHandler consistentMessageHandler;

        @Bean
        @ConditionalOnMissingBean
        public MessageCompensateProcessor messageCompensateProcessor() {
            DefaultMessageCompensateProcessor processor = new DefaultMessageCompensateProcessor(rabbitTemplate, consistentMessageHandler);
            startupCompensateSchedul(processor);
            return processor;
        }

        private void startupCompensateSchedul(DefaultMessageCompensateProcessor processor) {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleWithFixedDelay(processor::compensate, 10, 3, TimeUnit.MINUTES);
        }

    }

}
