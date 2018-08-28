package com.rb.sync;

import com.rb.core.message.JmsMessageSender;
import com.rb.core.message.ProviderMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MessageListenerTest {

    public static final String PROVIDER_ID = "hotel";
    
    private MessageListener messageListener;
    private MessageRepository messageRepository = new InMemoryMessageRepository();
    private JmsMessageSender jmsMessageSender = mock(JmsMessageSender.class);

    @Before
    public void setUp() {
        messageListener = new MessageListener(messageRepository, jmsMessageSender);
    }

    @Test
    public void onMessage_firstMessage() {
        messageListener.onMessage(createProviderMessage(1L));
        assertSentToConsumer(1L);
    }

    @Test
    public void onMessage_previousMessageDoesntExist() {
        messageListener.onMessage(createProviderMessage(2L));
        assertNotSentToConsumer(2L);
    }

    @Test
    public void onMessage_previousMessageExistsButNotSentToConsumer() {
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(1L)));
        messageListener.onMessage(createProviderMessage(2L));
        assertNotSentToConsumer(2L);
    }

    @Test
    public void onMessage_previousMessageExistsAndSentToConsumer() {
        ProviderMessageWrapper wrapedMessage = new ProviderMessageWrapper(createProviderMessage(1L));
        wrapedMessage.setSentToConsumer();
        messageRepository.save(wrapedMessage);
        messageListener.onMessage(createProviderMessage(2L));
        assertSentToConsumer(2L);
    }

    @Test
    public void onMessage_nextMessagesExist() {
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(2L)));
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(3L)));
        messageListener.onMessage(createProviderMessage(1L));
        assertSentToConsumer(1L, 2L, 3L);
    }

    @Test
    public void onMessage_previousAndNextMessagesExist() {
        ProviderMessageWrapper wrappedMessage = new ProviderMessageWrapper(createProviderMessage(1L));
        wrappedMessage.setSentToConsumer();
        messageRepository.save(wrappedMessage);
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(3L)));
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(4L)));
        messageListener.onMessage(createProviderMessage(2L));
        assertSentToConsumer(2L, 3L, 4L);
    }

    @Test
    public void onMessage_previousNotSentAndNextMessagesExist() {
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(1L)));
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(3L)));
        messageRepository.save(new ProviderMessageWrapper(createProviderMessage(4L)));
        messageListener.onMessage(createProviderMessage(2L));
        assertNotSentToConsumer(1L, 2L, 3L, 4L);
    }

    private ProviderMessage createProviderMessage(Long sequence) {
        ProviderMessage providerMessage = new ProviderMessage();
        providerMessage.setProviderId(PROVIDER_ID);
        providerMessage.setSequenceNumber(sequence);
        return providerMessage;
    }

    private void assertSentToConsumer(Long... sequences) {
        for (Long seq : sequences) {
            ProviderMessageWrapper message = messageRepository.getByProviderSequence(PROVIDER_ID, seq)
                    .orElseThrow(RuntimeException::new);
            Assert.assertTrue(message.isSentToConsumer());
            verify(jmsMessageSender, times(1)).send(message.getProviderMessage());
        }
    }

    private void assertNotSentToConsumer(Long... sequences) {
        for (Long seq : sequences) {
            ProviderMessageWrapper message = messageRepository.getByProviderSequence(PROVIDER_ID, seq)
                    .orElseThrow(RuntimeException::new);
            Assert.assertFalse(message.isSentToConsumer());
            verify(jmsMessageSender, never()).send(message.getProviderMessage());
        }
    }

}