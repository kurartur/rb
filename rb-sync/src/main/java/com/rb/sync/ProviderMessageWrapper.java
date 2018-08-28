package com.rb.sync;

import com.rb.core.message.ProviderMessage;

public class ProviderMessageWrapper {

    public enum Status {NEW, SENT_TO_CONSUMER}

    private ProviderMessage providerMessage;
    private Status status = Status.NEW;

    public ProviderMessageWrapper(ProviderMessage providerMessage) {
        this.providerMessage = providerMessage;
    }

    public ProviderMessage getProviderMessage() {
        return providerMessage;
    }

    public String getProviderId() {
        return providerMessage.getProviderId();
    }

    public Long getSequenceNumber() {
        return providerMessage.getSequenceNumber();
    }

    public void setSentToConsumer() {
        this.status = Status.SENT_TO_CONSUMER;
    }

    public boolean isNew() {
        return status == Status.NEW;
    }

    public boolean isSentToConsumer() {
        return status == Status.SENT_TO_CONSUMER;
    }

    @Override
    public String toString() {
        return "ProviderMessageWrapper{" +
                "providerMessage=" + providerMessage +
                ", status=" + status +
                '}';
    }
}
