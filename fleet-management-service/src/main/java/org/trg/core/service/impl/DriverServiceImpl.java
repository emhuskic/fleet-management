package org.trg.core.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.trg.core.domain.entity.DriverEntity;
import org.trg.core.domain.model.Driver;
import org.trg.core.repository.DriverRepository;
import org.trg.core.service.DriverService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DriverServiceImpl implements DriverService {

    @Inject
    DriverRepository driverRepository;

    @Override
    public Driver createDriver(final Driver driver) {
        DriverEntity entity = driver.toEntity();
        driverRepository.persist(entity);
        driverRepository.flush();
        return Driver.fromEntity(entity);
    }

    @Override
    public void deleteDriver(UUID id) {
        driverRepository.deleteDriver(id);
    }

    @Override
    public Optional<Driver> findDriver(UUID id) {
        return driverRepository.find("id", id).firstResultOptional().map(d -> Driver.fromEntity(d));
    }

    @Override
    public Driver getDriver(UUID id) {
        return findDriver(id).orElseThrow(() -> new NotFoundException(String.format("Driver with id=%s not found.", id)));
    }

    @Override
    public List<Driver> listDrivers() {
        return driverRepository.listAll().stream().map(d -> Driver.fromEntity(d)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void updateDriver(final UUID id, final Driver driver) {
        driverRepository.updateDriver(id, driver.toEntity());
    }
    
}
