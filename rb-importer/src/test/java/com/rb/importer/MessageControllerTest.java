package com.rb.importer;

import com.rb.core.message.JmsMessageSender;
import com.rb.core.message.ProviderMessage;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MessageControllerTest {

    private MessageController controller;
    private JmsMessageSender messageSender = mock(JmsMessageSender.class);

    @Before
    public void setUp() {
        controller = new MessageController(messageSender);
    }

    @Test
    public void message() {
        ProviderMessage providerMessage = new ProviderMessage();
        controller.message(providerMessage);
        verify(messageSender, times(1)).send(providerMessage);
    }
}