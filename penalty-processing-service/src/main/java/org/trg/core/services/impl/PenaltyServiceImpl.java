package org.trg.core.services.impl;

import org.trg.core.domain.Heartbeat;
import org.trg.core.services.PenaltyService;
import org.trg.core.services.RedisClient;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PenaltyServiceImpl implements PenaltyService {

    @Inject
    RedisClient redisClient;

    @Inject
    FineCalculationServiceImpl fineCalculationService;

    public Uni<Void> processPenalty(final Heartbeat heartbeat) {
        final Double fine = fineCalculationService.calculateFine(heartbeat.speed());
        return redisClient.increment(heartbeat.driverId(), fine).replaceWithVoid();
    }
}
