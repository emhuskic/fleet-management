package org.trg.core.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.trg.core.domain.entity.TripEntity;
import org.trg.core.domain.model.Car;
import org.trg.core.domain.model.Driver;
import org.trg.core.domain.model.Trip;
import org.trg.core.exception.ServiceException;
import org.trg.core.repository.TripRepository;
import org.trg.core.service.CarService;
import org.trg.core.service.DriverService;
import org.trg.core.service.MessageProducerService;
import org.trg.core.service.TripService;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TripServiceImpl implements TripService {

    @Inject
    TripRepository tripRepository;

    @Inject
    DriverService driverService;
    
    @Inject
    CarService carService;

    @Inject
    MessageProducerService<Trip> messageProducerService;

    @Override
    public Trip createTrip(final Driver driver, final Car car) throws ServiceException {
        if (driver == null || driver.isDeleted() || car == null || car.isDeleted()) {
            throw new ServiceException("Trip cannot be created with given driver or car.");
        }

        final List<TripEntity> existingTrips = tripRepository.findActiveTripsByDriverAndCar(driver.toEntity(), car.toEntity());
        if (existingTrips.size() > 0) {
            throw new ServiceException("Active trip already exists.");
        }

        Trip trip = new Trip();
        trip.setDriver(driver);
        trip.setCar(car);

        final TripEntity entity = trip.toEntity();
        tripRepository.persist(entity);
        tripRepository.flush();
        return Trip.fromEntity(entity);
    }

    @Override
    public Trip createTrip(final UUID driverId, final UUID carId) throws ServiceException {
        final Driver driver = driverService.getDriver(driverId);
        final Car car = carService.getCar(carId);
        if (driver == null || driver.isDeleted() || car == null || car.isDeleted()) {
            throw new ServiceException("Trip cannot be created with given driver or car.");
        }
        return createTrip(driver, car);
    }

    @Override
    public List<Trip> listActiveTrips() {
        return tripRepository.listActiveTrips().stream()
            .map(t -> Trip.fromEntity(t))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Trip> listAllTrips() {
        return tripRepository.listAll().stream()
            .map(t -> Trip.fromEntity(t))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Trip startTrip(final UUID id) {
        try {
            final Trip trip = Trip.fromEntity(tripRepository.startTrip(id));
            messageProducerService.produce(trip);
            return trip;
        } catch (final IllegalArgumentException e) {
            Log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        } catch (final Exception e) {
            Log.error(e);
            throw e;
        }
    }

    @Override
    public Trip startTrip(final UUID driverId, final UUID carId) {
        final Driver driver = driverService.getDriver(driverId);
        final Car car = carService.getCar(carId);
        if (driver == null || driver.isDeleted() || car == null || car.isDeleted()) {
            throw new ServiceException("Trip cannot be started with given driver or car.");
        }
        try {
            final Trip trip = Trip.fromEntity(tripRepository.startTrip(driver.toEntity(), car.toEntity()));
            messageProducerService.produce(trip);
            return trip;
        } catch (final IllegalArgumentException e) {
            Log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        } catch (final Exception e) {
            Log.error(e);
            throw e;
        }
    }

    @Override
    public Trip stopTrip(final UUID id) {
        try {
            final Trip trip = Trip.fromEntity(tripRepository.stopTrip(id));
            messageProducerService.produce(trip);
            return trip;
        } catch (final IllegalArgumentException e) {
            Log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        } catch (final Exception e) {
            Log.error(e);
            throw e;
        }
    }

    @Override
    public Trip stopTrip(final UUID driverId, final UUID carId) {
        try {
            final Driver driver = driverService.getDriver(driverId);
            final Car car = carService.getCar(carId);
            final Trip trip = Trip.fromEntity(tripRepository.stopTrip(driver.toEntity(), car.toEntity()));
            messageProducerService.produce(trip);
            return trip;
        } catch (final IllegalArgumentException e) {
            Log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        } catch (final Exception e) {
            Log.error(e);
            throw e;
        }
    }

}
