package org.trg.core.service;

import java.util.List;
import java.util.UUID;

import org.trg.core.domain.model.Trip;
import org.trg.core.domain.model.Driver;
import org.trg.core.domain.model.Car;

public interface TripService {
    Trip createTrip(Driver driver, Car car) throws Exception;
    Trip createTrip(UUID driverId, UUID carId) throws Exception;
    Trip startTrip(UUID id);
    Trip startTrip(UUID driverId, UUID carId);
    Trip stopTrip(UUID id);
    Trip stopTrip(UUID driverId, UUID carId);
    List<Trip> listAllTrips();
    List<Trip> listActiveTrips();
}
