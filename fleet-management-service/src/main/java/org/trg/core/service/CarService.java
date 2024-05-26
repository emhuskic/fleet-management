package org.trg.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.trg.core.domain.model.Car;

public interface CarService {
    List<Car> listAllCars();
    Car addCar(Car car);
    Optional<Car> findCar(UUID id);
    Car getCar(UUID id);
    void updateCar(UUID id, Car car);
    void deleteCar(UUID id);
}
