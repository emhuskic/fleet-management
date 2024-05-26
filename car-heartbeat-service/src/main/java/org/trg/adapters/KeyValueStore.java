package org.trg.adapters;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import org.trg.core.domain.Trip;

public interface KeyValueStore<T> {
    /**
     * put value in redis set for specific key
     * @param key
     * @param value
     * @return
     */
    Uni<Void> put(String key, T value);

    /**
     *  get all values from the set
     * @return
     */
    Multi<Trip> getAll();

    /**
     * Remove value from set
     * @param key
     * @return
     */
    Uni<Void> delete(String key);
}
