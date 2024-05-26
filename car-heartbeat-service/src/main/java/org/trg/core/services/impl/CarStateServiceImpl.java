package org.trg.core.services.impl;

import org.trg.core.domain.Heartbeat;
import org.trg.core.domain.Trip;
import org.trg.core.services.CarStateService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarStateServiceImpl implements CarStateService{

    @Override
    public Heartbeat getCarState(final Trip trip) {
        return new Heartbeat(trip.driver().id(), trip.car().id(), Math.random() * 150, Math.random(), Math.random());
    }
}
