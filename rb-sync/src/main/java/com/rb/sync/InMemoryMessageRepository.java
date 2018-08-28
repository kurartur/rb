package com.rb.sync;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryMessageRepository implements MessageRepository {

    // <provider id, <sequence num, message>>
    private Map<String, Map<Long, ProviderMessageWrapper>> messages = new HashMap<>();

    @Override
    public void save(ProviderMessageWrapper providerMessage) {
        String providerId = providerMessage.getProviderId();
        if (!messages.containsKey(providerId)) {
            messages.put(providerId, new HashMap<>());
        }
        messages.get(providerId).put(providerMessage.getSequenceNumber(), providerMessage);
    }

    @Override
    public Optional<ProviderMessageWrapper> getByProviderSequence(String providerId, Long sequence) {
        if (!messages.containsKey(providerId)) {
            return Optional.empty();
        }
        if (!messages.get(providerId).containsKey(sequence)) {
            return Optional.empty();
        }
        return Optional.of(messages.get(providerId).get(sequence));
    }

}
