package org.trg.core.domain.model;

import java.time.LocalDate;

import org.trg.core.domain.entity.DriverEntity;

public class Driver extends BaseModel {

    private String firstName;

    private String lastName;

    private String driversLicenseNo;

    private LocalDate dateOfBirth;
    private boolean deleted;

    public static Driver fromEntity(final DriverEntity entity) {
        if (entity == null) {
            return null;
        }
        Driver model = new Driver();
        model.setId(entity.getId());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setDriversLicenseNo(entity.getDriversLicenseNo());
        model.setDateOfBirth(entity.getDateOfBirth());
        model.setDeleted(entity.isDeleted());
        return model;
    }

    public DriverEntity toEntity() {
        DriverEntity entity = new DriverEntity();
        entity.setId(getId());
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setDriversLicenseNo(driversLicenseNo);
        entity.setDateOfBirth(dateOfBirth);
        entity.setDeleted(deleted);
        return entity;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getDriversLicenseNo() {
        return driversLicenseNo;
    }
    public void setDriversLicenseNo(final String driversLicenseNo) {
        this.driversLicenseNo = driversLicenseNo;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }
}
