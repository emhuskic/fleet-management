package org.trg.core.services.impl;

import java.util.Map;

import org.trg.core.services.FineCalculationService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FineCalculationServiceImpl implements FineCalculationService {

    // For every km over 60, we calculate penalty point = 2
    // For every km over 80, we calculate everything we do for 60 
    // + add additional 3 points for each over 80
    private final Map<Double, Integer> penalties = Map.of(
        80.0, 3,
        60.0, 2
    );

    public Double calculateFine(final Double speed) {
        return penalties.entrySet().stream()
                .filter(entry -> entry.getKey() < speed)
                .mapToDouble(entry -> entry.getValue() * Math.floor(speed - entry.getKey()) )
                .sum();
    }
}
