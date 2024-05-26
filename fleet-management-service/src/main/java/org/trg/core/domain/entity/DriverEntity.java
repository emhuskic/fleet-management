package org.trg.core.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class DriverEntity extends BaseEntity {

    private String firstName;

    private String lastName;

    private String driversLicenseNo;

    private LocalDate dateOfBirth;
    private boolean deleted;

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
