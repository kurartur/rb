package com.rb.importer;

import com.rb.core.message.JmsMessageSender;
import com.rb.core.message.ProviderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    private JmsMessageSender importerQueueSender;

    @Autowired
    public MessageController(JmsMessageSender importerQueueSender) {
        this.importerQueueSender = importerQueueSender;
    }

    @PostMapping("/message")
    public void message(@RequestBody ProviderMessage providerMessage) {
        logger.info(providerMessage.toString());
        importerQueueSender.send(providerMessage);
    }

}
