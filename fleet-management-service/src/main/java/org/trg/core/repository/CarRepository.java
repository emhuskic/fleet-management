package org.trg.core.repository;

import java.util.UUID;

import org.trg.core.domain.entity.CarEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

@jakarta.enterprise.context.ApplicationScoped
public class CarRepository implements PanacheRepository<CarEntity> {

    @Transactional
    public void updateCar(final UUID id, final CarEntity car) {
        update("model = ?1, color = ?2, manufacturer = ?3, licensePlate = ?4, deleted = ?5 where id = ?6",
               car.getModel(), car.getColor(), car.getManufacturer(), car.getLicensePlate(), car.isDeleted(), id);
    }

    @Transactional
    public void deleteCar(final UUID id) {
        update("deleted = true where id = ?1", id);
    }
}
