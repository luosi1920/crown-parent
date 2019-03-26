package com.yibai.crown.rabbitmq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class RabbitConditionListenerConfiguration {

    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer() {
        return registrar -> registrar.setEndpointRegistry(endpointRegistry());
    }

    @Bean
    public RabbitListenerEndpointRegistry endpointRegistry() {
        return new RabbitListenerEndpointRegistry() {
            @Override
            public void registerListenerContainer(RabbitListenerEndpoint endpoint, RabbitListenerContainerFactory<?> factory) {
                //                if (endpoint instanceof MethodRabbitListenerEndpoint) {
                //                    MethodRabbitListenerEndpoint methodEndpoint = (MethodRabbitListenerEndpoint) endpoint;
                //                    Method method = methodEndpoint.getMethod();
                //                    boolean present = method.isAnnotationPresent(RabbitListener.class) && method.isAnnotationPresent(UniverseExclusive.class);
                //                    if (present) {
                //                        RabbitListener listener = method.getAnnotation(RabbitListener.class);
                //                        String[] queues = listener.queues();
                //                        if (tryExclusive(queues)) {
                //                            super.registerListenerContainer(endpoint, factory);
                //                        } else {
                //                            log.info("尝试注册独占消费端失败,已被独占注册,queues:{}", Arrays.toString(queues));
                //                        }
                //                    } else {
                //                        super.registerListenerContainer(endpoint, factory);
                //                    }
                //                } else {
                //                    super.registerListenerContainer(endpoint, factory);
                //                }
                super.registerListenerContainer(endpoint, factory);
            }
        };
    }

    private boolean tryExclusive(String[] queues) {
        return false;
    }

}
