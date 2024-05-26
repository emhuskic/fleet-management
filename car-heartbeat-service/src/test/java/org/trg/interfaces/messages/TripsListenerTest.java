package org.trg.interfaces.messages;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trg.core.client.KeyValueClient;
import org.trg.core.domain.Car;
import org.trg.core.domain.Driver;
import org.trg.core.domain.Trip;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;

@QuarkusTest
class TripsListenerTest {

    private TripsListener tripsListener;
    private KeyValueClient keyValueClient;

    @BeforeEach
    void setUp() {
        keyValueClient = mock(KeyValueClient.class);
        tripsListener = new TripsListener();
        tripsListener.keyValueStore = keyValueClient;
    }

    @Test
    void testConsumeTrip_WithNullTrip_returnsNull() {
        final Uni<Void> result = tripsListener.consume(null);
        assertNull(result);
    }

    @Test
    void testConsumeTrip_EndTimeNotNull() {
        final UUID driverId = UUID.randomUUID();
        final UUID tripId = UUID.randomUUID();
        final UUID carId = UUID.randomUUID();
        final Trip trip = new Trip(
            tripId, 
            new Driver(driverId, "John", "Doe", null, null, false), 
            new Car(carId, "licensePlate", "model", "color", "", false), 
            LocalDateTime.now(), 
            LocalDateTime.now()
        );        
        when(keyValueClient.delete(tripId.toString())).thenReturn(Uni.createFrom().nullItem());

        final Uni<Void> result = tripsListener.consume(trip);
        assertNotNull(result);
        verify(keyValueClient, times(1)).delete(tripId.toString());
    }

    @Test
    void testConsumeTrip_StartTimeNotNull() {
        final UUID driverId = UUID.randomUUID();
        final UUID tripId = UUID.randomUUID();
        final UUID carId = UUID.randomUUID();
        final Trip trip = new Trip(
            tripId, 
            new Driver(driverId, "John", "Doe", null, null, false), 
            new Car(carId, "licensePlate", "model", "color", "", false), 
            LocalDateTime.now(), 
            null
        );     
        when(keyValueClient.put(tripId.toString(), trip)).thenReturn(Uni.createFrom().nullItem());

        final Uni<Void> result = tripsListener.consume(trip);
        assertNotNull(result);
        verify(keyValueClient, times(1)).put(tripId.toString(), trip);
    }

    @Test
    void testConsumeTrip_StartTimeEndTimeNull_returnsNull() {
        final UUID driverId = UUID.randomUUID();
        final UUID tripId = UUID.randomUUID();
        final UUID carId = UUID.randomUUID();
        final Trip trip = new Trip(
            tripId, 
            new Driver(driverId, "John", "Doe", null, null, false), 
            new Car(carId, "licensePlate", "model", "color", "", false), 
            null, 
            null
        );     
        when(keyValueClient.put(tripId.toString(), trip)).thenReturn(Uni.createFrom().nullItem());

        final Uni<Void> result = tripsListener.consume(trip);        
        assertNull(result);
    }
}
