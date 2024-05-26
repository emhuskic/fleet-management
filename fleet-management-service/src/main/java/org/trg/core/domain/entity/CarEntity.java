package org.trg.core.domain.entity;

import jakarta.persistence.Entity;

@Entity
public class CarEntity extends BaseEntity {
    private String licensePlate;

    private String model;
    private String color;
    private String manufacturer;
    private boolean deleted;

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(final String licensePlate) {
        this.licensePlate = licensePlate;
    }
    public String getModel() {
        return model;
    }
    public void setModel(final String model) {
        this.model = model;
    }
    public String getColor() {
        return color;
    }
    public void setColor(final String color) {
        this.color = color;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
    
    
}
