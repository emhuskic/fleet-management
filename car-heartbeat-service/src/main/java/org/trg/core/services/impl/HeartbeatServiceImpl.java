package org.trg.core.services.impl;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.trg.core.client.KeyValueClient;
import org.trg.core.domain.Heartbeat;
import org.trg.core.services.CarStateService;

@ApplicationScoped
public class HeartbeatServiceImpl {
    
    @Inject
    @Channel("heartbeat")
    Emitter<Heartbeat> heartbeatEmitter;

    @Inject
    KeyValueClient keyValueStore;

    @Inject
    CarStateService carStateService;

    /**
     * Scheduled each N seconds
     * Goes through all records in data store
     * For each of them - sends a "randomized car state" message
     */
    @Scheduled(every = "${heartbeat.interval}", delayed = "${heartbeat.delayed}")
    public void performHeartbeat()  {
        keyValueStore.getAll().subscribe().with(trip -> {
            final Heartbeat heartbeat = carStateService.getCarState(trip);
            heartbeatEmitter.send(heartbeat);
        }, error -> {
            Log.error(error);
        });
    }
}
