package org.trg.interfaces.messages;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.trg.core.client.KeyValueClient;
import org.trg.core.domain.Trip;

@ApplicationScoped
public class TripsListener {

    @Inject
    KeyValueClient keyValueStore;

    /**
     * Consumes Trip from Message bus
     * Adds Trip to the data store if it started
     * and deletes it if it has ended
     * 
     * @param trip
     * @return
     */
    @Incoming("trips")
    public Uni<Void> consume(final Trip trip) {
        if (trip == null) {
            return null;
        }
        if (trip.endTime() != null) {
            return keyValueStore.delete(trip.id().toString());
        }
        if (trip.startTime() != null) {
            return keyValueStore.put(trip.id().toString(), trip);
        }
        return null;
    }
}
