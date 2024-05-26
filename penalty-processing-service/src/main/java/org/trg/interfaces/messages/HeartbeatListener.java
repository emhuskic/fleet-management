package org.trg.interfaces.messages;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.trg.core.domain.Heartbeat;
import org.trg.core.services.PenaltyService;

@ApplicationScoped
public class HeartbeatListener {

    @Inject
    PenaltyService penaltyService;

    /**
     * Consumes heartbeat message
     * Processes penalty for the heartbeat
     * 
     * @param heartbeat
     * @return
     */
    @Incoming("heartbeat")
    public Uni<Void> consume(final Heartbeat heartbeat) throws Exception {
        if (heartbeat == null) {
            return Uni.createFrom().nothing();
        }
        Log.debug("Got a message for driver=" + heartbeat.driverId() + " with speed=" + heartbeat.speed());
        try {
            return penaltyService.processPenalty(heartbeat);
        } catch (final Exception exception) {
            Log.error("Error processing penalty, error=" + exception.getMessage());
            throw exception;
        }
    }
}
