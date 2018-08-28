package com.rb.sync;

import com.rb.core.message.JmsMessageSender;
import com.rb.core.message.ProviderMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageListener {

    private MessageRepository messageRepository;
    private JmsMessageSender consumerQueueSender;

    @Autowired
    public MessageListener(MessageRepository messageRepository, JmsMessageSender consumerQueueSender) {
        this.messageRepository = messageRepository;
        this.consumerQueueSender = consumerQueueSender;
    }

    @RabbitListener(queues = "${rb.importer_queue_name}")
    public void onMessage(ProviderMessage newMessage) {
        String providerId = newMessage.getProviderId();
        Long currentSequence = newMessage.getSequenceNumber();

        // if its not the first message and previous message doesn't exists or is not sent to consumer
        // then save the message without sending
        // else send current and next messages
        Optional<ProviderMessageWrapper> previousMessageOptional = messageRepository.getByProviderSequence(providerId, currentSequence - 1);
        if (currentSequence != 1L && (!previousMessageOptional.isPresent() || !previousMessageOptional.get().isSentToConsumer())) {
            saveMessage(new ProviderMessageWrapper(newMessage));
        } else {
            sendToConsumerAndSave(new ProviderMessageWrapper(newMessage));
            sendNextMessages(providerId, currentSequence);
        }
    }

    private void sendNextMessages(String providerId, Long currentSequence) {
        Long nextSequence = currentSequence + 1;
        Optional<ProviderMessageWrapper> nextMessageOptional = messageRepository.getByProviderSequence(providerId, nextSequence);
        while(nextMessageOptional.isPresent()) {
            sendToConsumerAndSave(nextMessageOptional.get());
            nextMessageOptional = messageRepository.getByProviderSequence(providerId, ++nextSequence);
        }
    }

    private void sendToConsumerAndSave(ProviderMessageWrapper wrappedMessage) {
        consumerQueueSender.send(wrappedMessage.getProviderMessage());
        wrappedMessage.setSentToConsumer();
        saveMessage(wrappedMessage);
    }

    private void saveMessage(ProviderMessageWrapper wrappedMessage) {
        messageRepository.save(wrappedMessage);
    }

}
