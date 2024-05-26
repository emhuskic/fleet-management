package org.trg.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.trg.core.domain.entity.CarEntity;
import org.trg.core.domain.entity.DriverEntity;
import org.trg.core.domain.entity.TripEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;

@jakarta.enterprise.context.ApplicationScoped
public class TripRepository implements PanacheRepositoryBase<TripEntity, UUID> {

    @Transactional
    public TripEntity startTrip(UUID id) {
        TripEntity trip = findById(id);
        if (trip != null && trip.getStartTime() == null) {
            trip.setStartTime(LocalDateTime.now());
            persist(trip);
            return trip;
        } else {
            throw new IllegalArgumentException("Trip with id " + id + " not found or already started");
        }
    }

    @Transactional
    public TripEntity stopTrip(UUID id) {
        TripEntity trip = findById(id);
        if (trip != null && trip.getEndTime() == null) {
            trip.setEndTime(LocalDateTime.now());
            persist(trip);
            return trip;
        } else {
            throw new IllegalArgumentException("Trip with id " + id + " not found or already stopped");
        }
    }

    @Transactional
    public TripEntity startTrip(DriverEntity driver, CarEntity car) {
        // Find an existing trip for the given driver and car where the start time is null
        Optional<TripEntity> existingTrip = find("driver = ?1 and car = ?2 and startTime is null", driver, car).firstResultOptional();
        
        if (existingTrip.isPresent()) {
            // If an existing trip is found, update its start time and return it
            TripEntity trip = existingTrip.get();
            trip.setStartTime(LocalDateTime.now());
            persist(trip);
            flush();
            return trip;
        } else {
            // If no existing trip is found, create a new trip and set its properties
            TripEntity newTrip = new TripEntity();
            newTrip.setDriver(driver);
            newTrip.setCar(car);
            newTrip.setStartTime(LocalDateTime.now());
            persist(newTrip);
            flush();
            return newTrip;
        }
    }

    @Transactional
    public TripEntity stopTrip(DriverEntity driver, CarEntity car) {
        // Find an existing trip for the given driver and car where the start time is null
        Optional<TripEntity> existingTrip = find("driver = ?1 and car = ?2 and endTime is null", driver, car).firstResultOptional();
        
        if (existingTrip.isPresent()) {
            // If an existing trip is found, update its start time and return it
            TripEntity trip = existingTrip.get();
            trip.setEndTime(LocalDateTime.now());
            persist(trip);
            flush();
            return trip;
        } else {
            throw new IllegalArgumentException("Trip with driver=" + driver.getId() + " and car=" + car.getId() + " not found or already stopped");
        }
    }

    public List<TripEntity> listActiveTrips() {
        return find("startTime is not null and endTime is null").list();
    }

    public List<TripEntity> findTripsByDriver(DriverEntity driver) {
        return find("driver = ?1", driver).list();
    }

    public List<TripEntity> findTripsByCar(CarEntity car) {
        return find("car = ?1", car).list();
    }

    public List<TripEntity> findActiveTripsByDriverAndCar(DriverEntity driver, CarEntity car) {
        return find("driver = ?1 and car = ?2 and endTime is null", driver, car).list();
    }
}
