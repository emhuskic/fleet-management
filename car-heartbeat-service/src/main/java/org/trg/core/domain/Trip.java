package org.trg.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Trip(UUID id, Driver driver, Car car, LocalDateTime startTime, LocalDateTime endTime) {
}
