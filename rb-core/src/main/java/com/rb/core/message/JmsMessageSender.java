package com.rb.core.message;

public interface JmsMessageSender {
    void send(ProviderMessage providerMessage);
}
