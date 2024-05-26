package org.trg.core.domain;

import java.time.LocalDate;
import java.util.UUID;

public record Driver(UUID id, String firstName, String lastName, String driversLicenseNo, LocalDate dateOfBirth, boolean deleted) { }
