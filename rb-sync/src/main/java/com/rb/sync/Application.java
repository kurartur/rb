package com.rb.sync;

import com.rb.core.message.JmsMessageSender;
import com.rb.core.message.RabbitMessageSender;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Bean
    protected JmsMessageSender consumerQueueSender(RabbitTemplate rabbitTemplate, @Value("${rb.consumer_queue_name}") String queueName) {
        return new RabbitMessageSender(rabbitTemplate, new Queue(queueName));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
