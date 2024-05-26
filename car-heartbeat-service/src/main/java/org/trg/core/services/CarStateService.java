package org.trg.core.services;

import org.trg.core.domain.Heartbeat;
import org.trg.core.domain.Trip;

public interface CarStateService {
    /**
     * Gets current car state for a certain trip
     * 
     * @param trip
     * @return
     */
    Heartbeat getCarState(Trip trip);
}
