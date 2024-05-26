package org.trg.core.services.impl;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

import org.trg.core.services.RedisClient;

@ApplicationScoped
public class PenaltyRedisClient implements RedisClient {
    private static final String PENALTY_PREFIX = "penalty-";
    private final ReactiveValueCommands<String, Double> valueCommands;

    public PenaltyRedisClient(final ReactiveRedisDataSource reactive) {
        valueCommands = reactive.value(String.class, Double.class);
    }

    public Uni<Double> get(final String driverId) {
        return valueCommands.get(PENALTY_PREFIX + driverId)
            .map(value -> value != null ? value : 0.0);
    }

    public Uni<Double> increment(final UUID key, final Double newPenalty) {
        return valueCommands.incrbyfloat(PENALTY_PREFIX + key.toString(), newPenalty);
    }
}
