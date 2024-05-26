package org.trg.core.domain.model;

import java.time.LocalDateTime;

import org.trg.core.domain.entity.TripEntity;

public class Trip extends BaseModel {

    private Driver driver;

    private Car car;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public static Trip fromEntity(final TripEntity entity) {
        if (entity == null) {
            return null;
        }
        Trip model = new Trip();
        model.setId(entity.getId());
        model.setCar(Car.fromEntity(entity.getCar()));
        model.setDriver(Driver.fromEntity(entity.getDriver()));
        model.setStartTime(entity.getStartTime());
        model.setEndTime(entity.getEndTime());
        return model;
    }

    public TripEntity toEntity() {
        TripEntity entity = new TripEntity();
        entity.setId(getId());
        entity.setCar(car.toEntity());
        entity.setDriver(driver.toEntity());
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
        return entity;
    }

    public Driver getDriver() {
        return driver;
    }
    public void setDriver(final Driver driver) {
        this.driver = driver;
    }
    public Car getCar() {
        return car;
    }
    public void setCar(final Car car) {
        this.car = car;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(final LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
