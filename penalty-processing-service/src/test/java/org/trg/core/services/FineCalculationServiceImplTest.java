package org.trg.core.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.trg.core.services.impl.FineCalculationServiceImpl;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class FineCalculationServiceImplTest {

    @InjectMocks
    private FineCalculationServiceImpl fineCalculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateFine_SpeedBelow60() {
        double speed = 55.0;
        double expectedFine = 0.0; // No fine since speed is below 60
        double actualFine = fineCalculationService.calculateFine(speed);
        assertEquals(expectedFine, actualFine);
    }

    @Test
    void testCalculateFine_SpeedExactly60() {
        double speed = 60.0;
        double expectedFine = 0.0; // No fine since speed is exactly 60
        double actualFine = fineCalculationService.calculateFine(speed);
        assertEquals(expectedFine, actualFine);
    }

    @Test
    void testCalculateFine_SpeedBetween60And80() {
        double speed = 70.0;
        double expectedFine = 20.0; // (70-60) * 2
        double actualFine = fineCalculationService.calculateFine(speed);
        assertEquals(expectedFine, actualFine);
    }

    @Test
    void testCalculateFine_SpeedExactly80() {
        double speed = 80.0;
        double expectedFine = 40.0; // (80-60) * 2 + (80-80) * 3
        double actualFine = fineCalculationService.calculateFine(speed);
        assertEquals(expectedFine, actualFine);
    }

    @Test
    void testCalculateFine_SpeedAbove80() {
        double speed = 90.0;
        double expectedFine = 90.0; // (90-60) * 2 + (90-80) * 3 = 60 + 30
        double actualFine = fineCalculationService.calculateFine(speed);
        assertEquals(expectedFine, actualFine);
    }
}
