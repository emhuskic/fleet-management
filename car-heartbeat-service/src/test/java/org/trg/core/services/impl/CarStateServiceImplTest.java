package org.trg.core.services.impl;

import org.junit.jupiter.api.Test;
import org.trg.core.domain.Car;
import org.trg.core.domain.Driver;
import org.trg.core.domain.Heartbeat;
import org.trg.core.domain.Trip;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

@QuarkusTest
class CarStateServiceImplTest {

    @Inject
    private CarStateServiceImpl carStateService;

    @Test
    void getCarState_ReturnsHeartbeat() {
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

        Heartbeat heartbeat = carStateService.getCarState(trip);

        assertEquals(trip.driver().id(), heartbeat.driverId());
        assertEquals(trip.car().id(), heartbeat.carId());
        assertNotNull(heartbeat.speed());
    }
}
