package org.trg.core.repository;

import java.util.UUID;

import org.trg.core.domain.entity.DriverEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

@jakarta.enterprise.context.ApplicationScoped
public class DriverRepository implements PanacheRepository<DriverEntity> {

    @Transactional
    public void updateDriver(final UUID id, final DriverEntity updatedDriver) {
        update("firstName = ?1, lastName = ?2, driversLicenseNo = ?3, dateOfBirth = ?4, deleted = ?5 where id = ?6",
               updatedDriver.getFirstName(), updatedDriver.getLastName(), 
               updatedDriver.getDriversLicenseNo(), updatedDriver.getDateOfBirth(), 
               updatedDriver.isDeleted(), id);
    }

    @Transactional
    public void deleteDriver(final UUID id) {
        update("deleted = true where id = ?1", id);
    }
}
