package org.trg.core.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Entity
public class TripEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private DriverEntity driver;

    @ManyToOne(fetch = FetchType.LAZY)
    private CarEntity car;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public DriverEntity getDriver() {
        return driver;
    }
    public void setDriver(final DriverEntity driver) {
        this.driver = driver;
    }
    public CarEntity getCar() {
        return car;
    }
    public void setCar(final CarEntity car) {
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
