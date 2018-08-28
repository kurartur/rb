package com.rb.sync;

import java.util.Optional;

public interface MessageRepository {

    void save(ProviderMessageWrapper providerMessage);

    Optional<ProviderMessageWrapper> getByProviderSequence(String providerId, Long sequence);

}
