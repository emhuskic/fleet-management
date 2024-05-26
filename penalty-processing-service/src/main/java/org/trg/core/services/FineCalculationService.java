package org.trg.core.services;

public interface FineCalculationService {
    /**
     * Fine calculation for given speed
     * @param speed
     * @return Returns calculated points
     */
    Double calculateFine(final Double speed);
} 
