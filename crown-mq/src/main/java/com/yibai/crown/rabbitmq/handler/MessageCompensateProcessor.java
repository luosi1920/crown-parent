package com.yibai.crown.rabbitmq.handler;

/**
 * MQ消息补偿处理
 */
public interface MessageCompensateProcessor {

    void compensate();

}
