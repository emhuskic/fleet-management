package org.trg.core.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.trg.core.domain.entity.CarEntity;
import org.trg.core.domain.model.Car;
import org.trg.core.repository.CarRepository;
import org.trg.core.service.CarService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CarServiceImpl implements CarService {
    @Inject
    CarRepository carRepository;

    @Override
    public List<Car> listAllCars() {
        return carRepository.listAll().stream()
            .map(car -> Car.fromEntity(car))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Car addCar(final Car car) {
        CarEntity entity = car.toEntity();
        carRepository.persist(entity);
        carRepository.flush();
        return Car.fromEntity(entity);
    }

    @Override
    public Optional<Car> findCar(final UUID id) {
        return carRepository.find("id", id).firstResultOptional().map(c -> Car.fromEntity(c));
    }

    @Override
    public Car getCar(UUID id) {
        return findCar(id).orElseThrow(() -> new NotFoundException(String.format("Car with id=%s not found.", id)));
    }

    @Override
    public void updateCar(final UUID id, final Car car) {
        carRepository.updateCar(id, car.toEntity());
    }

    @Override
    public void deleteCar(final UUID id) {
        carRepository.deleteCar(id);
    }
    
}
