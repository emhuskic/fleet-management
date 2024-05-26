package org.trg.core.services;

import org.trg.core.domain.Heartbeat;

import io.smallrye.mutiny.Uni;

public interface PenaltyService {
    /**
     * Processes penalty for each heartbeat received
     * 
     * @param heartbeat
     * @return
     */
    Uni<Void> processPenalty(final Heartbeat heartbeat);
}