package org.trg.core.client;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import org.trg.adapters.KeyValueStore;
import org.trg.core.domain.Trip;

@ApplicationScoped
public class KeyValueClient implements KeyValueStore<Trip> {
    private static final String TRIP_PREFIX = "trips-";

    private final ReactiveKeyCommands<String> keyCommands;
    private final ReactiveValueCommands<String, Trip> valueCommands;

    public KeyValueClient(final ReactiveRedisDataSource reactive) {
        keyCommands = reactive.key(String.class);
        valueCommands = reactive.value(String.class, Trip.class);
    }

    @Override
    public Uni<Void> put(final String key, final Trip value) {
        return valueCommands.set(TRIP_PREFIX + key, value).replaceWithVoid();
    }

    @Override
    public Multi<Trip> getAll() {
        return keyCommands.keys(TRIP_PREFIX + "*")
            .onItem().transformToMulti(keys -> Multi.createFrom().items(keys::stream))
            .onItem().transformToUniAndMerge(valueCommands::get);
    }

    @Override
    public Uni<Void> delete(final String key) {
        return keyCommands.del(TRIP_PREFIX+key).replaceWithVoid();
    }
}
