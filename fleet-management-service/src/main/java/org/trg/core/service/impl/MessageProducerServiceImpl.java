package org.trg.core.service.impl;

import java.io.Serializable;

import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.trg.core.service.MessageProducerService;

import io.smallrye.context.api.CurrentThreadContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class MessageProducerServiceImpl<T extends Serializable> implements MessageProducerService<T> {

    @Inject
    @Channel("trips")
    Emitter<T> emitter;

    @Override
    @CurrentThreadContext(cleared = { ThreadContext.TRANSACTION })
    public void produce(final T message) {
        emitter.send(message).toCompletableFuture().join();
    }
}
