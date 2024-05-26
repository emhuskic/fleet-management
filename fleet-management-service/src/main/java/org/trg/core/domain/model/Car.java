package org.trg.core.domain.model;

import org.trg.core.domain.entity.CarEntity;

public class Car extends BaseModel {
    private String licensePlate;

    private String model;
    private String color;
    private String manufacturer;
    private boolean deleted;

    public static Car fromEntity(final CarEntity entity) {
        if (entity == null) {
            return null;
        }
        Car model = new Car();
        model.setId(entity.getId());
        model.setLicensePlate(entity.getLicensePlate());
        model.setColor(entity.getColor());
        model.setManufacturer(entity.getManufacturer());
        model.setModel(entity.getModel());
        model.setDeleted(entity.isDeleted());
        return model;
    }

    public CarEntity toEntity() {
        CarEntity entity = new CarEntity();
        entity.setId(getId());
        entity.setLicensePlate(licensePlate);
        entity.setColor(color);
        entity.setManufacturer(manufacturer);
        entity.setModel(model);
        entity.setDeleted(deleted);
        return entity;
    }

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
