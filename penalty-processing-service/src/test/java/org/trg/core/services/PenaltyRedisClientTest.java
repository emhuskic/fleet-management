package org.trg.core.services;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.trg.core.services.impl.PenaltyRedisClient;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
public class PenaltyRedisClientTest {

    @Mock
    ReactiveRedisDataSource reactiveRedisDataSourceMock;

    @Mock
    ReactiveValueCommands<String, Double> valueCommandsMock;

    @InjectMocks
    PenaltyRedisClient penaltyRedisClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(reactiveRedisDataSourceMock.value(String.class, Double.class)).thenReturn(valueCommandsMock);
        penaltyRedisClient = new PenaltyRedisClient(reactiveRedisDataSourceMock);
    }

    @Test
    public void testGet() {
        final String driverId = "driver123";
        final String key = "penalty-" + driverId;
        final Double expectedFine = 15.0;

        when(valueCommandsMock.get(key)).thenReturn(Uni.createFrom().item(expectedFine));

        final Uni<Double> result = penaltyRedisClient.get(driverId);

        assertEquals(expectedFine, result.await().indefinitely());
    }

    @Test
    public void testGet_ReturnsDefaultValueWhenNull() {
        final String driverId = "driver123";
        final String key = "penalty-" + driverId;
        final Double defaultFine = 0.0;

        when(valueCommandsMock.get(key)).thenReturn(Uni.createFrom().nullItem());

        final Uni<Double> result = penaltyRedisClient.get(driverId);

        assertEquals(defaultFine, result.await().indefinitely());
    }

    @Test
    public void testIncrement() {
        final UUID driverId = UUID.randomUUID();
        final String key = "penalty-" + driverId;
        final Double newPenalty = 10.0;
        final Double updatedFine = 25.0;

        when(valueCommandsMock.incrbyfloat(key, newPenalty)).thenReturn(Uni.createFrom().item(updatedFine));

        final Uni<Double> result = penaltyRedisClient.increment(driverId, newPenalty);

        assertEquals(updatedFine, result.await().indefinitely());
    }
}
