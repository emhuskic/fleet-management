package org.trg.core.domain.dto;

import java.io.Serializable;
import java.util.UUID;


public record TripUpdateDto(UUID id, UUID driverId, UUID carId) implements Serializable {
} 