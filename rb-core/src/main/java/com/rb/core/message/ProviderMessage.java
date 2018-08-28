package com.rb.core.message;

import java.io.Serializable;

public class ProviderMessage implements Serializable {

    private String providerId;
    private Long sequenceNumber;

    private String payload;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "ProviderMessage{" +
                "providerId='" + providerId + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", payload='" + payload + '\'' +
                '}';
    }
}
