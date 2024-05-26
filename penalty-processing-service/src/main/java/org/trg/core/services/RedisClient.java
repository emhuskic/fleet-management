package org.trg.core.services;

import java.util.UUID;

import io.smallrye.mutiny.Uni;

public interface RedisClient {
    
    /**
     * Gets value from key-value map
     * for given key
     * @param id
     * @return
     */
    Uni<Double> get(String id);

    /**
     * Increments value for given key
     * by given value
     * @param key
     * @param value
     * @return
     */
    Uni<Double> increment(final UUID key, final Double value);
} 
