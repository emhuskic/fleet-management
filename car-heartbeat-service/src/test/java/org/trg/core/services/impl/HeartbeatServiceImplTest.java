package org.trg.core.services.impl;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trg.core.client.KeyValueClient;
import org.trg.core.domain.Car;
import org.trg.core.domain.Driver;
import org.trg.core.domain.Heartbeat;
import org.trg.core.domain.Trip;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class HeartbeatServiceImplTest {

    @Mock
    KeyValueClient keyValueStore;

    @Mock
    CarStateServiceImpl carStateService;

    @Mock
    Emitter<Heartbeat> heartbeatEmitter;;

    @InjectMocks
    HeartbeatServiceImpl heartbeatService;

    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPerformHeartbeat() {
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
        Multi<Trip> testMultiTrip = Multi.createFrom().item(trip);
        when(keyValueStore.getAll()).thenReturn(testMultiTrip);

        Heartbeat dummyHeartbeat = new Heartbeat(UUID.randomUUID(), UUID.randomUUID(), 50.0, 0.0, 0.0);
        when(carStateService.getCarState(Mockito.any())).thenReturn(dummyHeartbeat);

        heartbeatService.performHeartbeat();

        verify(keyValueStore, times(1)).getAll();
        verify(carStateService, times(1)).getCarState(Mockito.any());
    }

    @Test
    public void testPerformHeartbeat_noTripsFound() {
        Multi<Trip> testMultiTrip = Multi.createFrom().empty();
        when(keyValueStore.getAll()).thenReturn(testMultiTrip);
        heartbeatService.performHeartbeat();

        verify(keyValueStore, times(1)).getAll();
        verify(carStateService, times(0)).getCarState(Mockito.any());
    }

    @Test
    public void testPerformHeartbeat_error_doNothing() {
        Multi<Trip> testMultiTrip = Multi.createFrom().failure(new RuntimeException("Error occurred"));
        when(keyValueStore.getAll()).thenReturn(testMultiTrip);
        heartbeatService.performHeartbeat();

        verify(keyValueStore, times(1)).getAll();
        verify(carStateService, times(0)).getCarState(Mockito.any());
    }
}
