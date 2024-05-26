package org.trg.core.domain;

import java.util.UUID;

public record Car(UUID id, String licensePlate, String model, String color, String manufacturer, boolean deleted) {
}
