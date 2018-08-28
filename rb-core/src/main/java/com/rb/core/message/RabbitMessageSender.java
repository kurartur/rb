package com.rb.core.message;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMessageSender implements JmsMessageSender {

    private RabbitTemplate rabbitTemplate;
    private Queue queue;

    public RabbitMessageSender(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    @Override
    public void send(ProviderMessage providerMessage) {
        rabbitTemplate.convertAndSend(queue.getName(), providerMessage);
    }

}
