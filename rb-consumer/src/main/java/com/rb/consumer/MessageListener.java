package com.rb.consumer;

import com.rb.core.message.ProviderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = "${rb.consumer_queue_name}")
    public void onMessageReceived(ProviderMessage providerMessage) {
        logger.info(providerMessage.toString());
        // Do stuff here
    }

}
