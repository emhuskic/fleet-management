package org.trg.core.domain;

import java.util.UUID;

public record Heartbeat(UUID driverId, UUID carId, Double speed, Double latitude, Double longitude) {}
