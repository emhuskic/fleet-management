package org.trg.core.domain.dto;

import java.io.Serializable;
import java.util.UUID;

public record TripCreateDto(UUID driverId, UUID carId) implements Serializable {
    
}
