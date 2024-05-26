package org.trg.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.trg.core.domain.model.Driver;

public interface DriverService {
    List<Driver> listDrivers();
    Driver createDriver(Driver driver);
    Driver getDriver(UUID id);
    Optional<Driver> findDriver(UUID id);
    void updateDriver(UUID id, Driver car);
    void deleteDriver(UUID id);
}
