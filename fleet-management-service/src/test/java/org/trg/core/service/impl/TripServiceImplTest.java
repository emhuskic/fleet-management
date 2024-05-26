package org.trg.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.trg.core.domain.entity.TripEntity;
import org.trg.core.domain.model.Car;
import org.trg.core.domain.model.Driver;
import org.trg.core.domain.model.Trip;
import org.trg.core.exception.ServiceException;
import org.trg.core.repository.TripRepository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class TripServiceImplTest {

    @Inject
    TripServiceImpl tripService;

    @InjectMock
    TripRepository tripRepository;

    @InjectMock
    DriverServiceImpl driverService;

    @InjectMock
    CarServiceImpl carService;

    private Driver driver;
    private Car car;
    private TripEntity tripEntity;
    private UUID tripId;
    private UUID driverId;
    private UUID carId;

    @Captor
    ArgumentCaptor<Message<Trip>> messageCaptor;

    @BeforeEach
    public void setUp() {
        tripId = UUID.randomUUID();
        driverId = UUID.randomUUID();
        carId = UUID.randomUUID();

        driver = new Driver();
        driver.setId(driverId);
        driver.setFirstName("John");
        driver.setLastName("Doe");

        car = new Car();
        car.setId(carId);
        car.setModel("Model S");
        car.setLicensePlate("123-ABC");

        tripEntity = new TripEntity();
        tripEntity.setId(tripId);
        tripEntity.setDriver(driver.toEntity());
        tripEntity.setCar(car.toEntity());
        tripEntity.setStartTime(null);
        tripEntity.setEndTime(null);
    }

    @Test
    public void testCreateTrip_Success() throws ServiceException {
        Driver driver = new Driver();
        Car car = new Car();
        when(tripRepository.findActiveTripsByDriverAndCar(any(), any())).thenReturn(Collections.emptyList());

        Trip createdTrip = tripService.createTrip(driver, car);

        assertNotNull(createdTrip);
        verify(tripRepository, times(1)).persist(any(TripEntity.class));
        verify(tripRepository, times(1)).flush();
    }

    @Test
    public void testCreateTrip_ActiveTripExists() throws ServiceException {
        Driver driver = new Driver();
        Car car = new Car();
        List<TripEntity> existingTrips = Collections.singletonList(new TripEntity());
        when(tripRepository.findActiveTripsByDriverAndCar(any(), any())).thenReturn(existingTrips);

        assertThrows(ServiceException.class, () -> tripService.createTrip(driver, car));
        verify(tripRepository, never()).persist(any(TripEntity.class));
        verify(tripRepository, never()).flush();
    }

    @Test
    public void testCreateTrip_DriverDeleted() throws ServiceException {
        Driver driver = new Driver();
        driver.setDeleted(true);
        Car car = new Car();
        assertThrows(ServiceException.class, () -> tripService.createTrip(driver, car));
        verify(tripRepository, never()).persist(any(TripEntity.class));
        verify(tripRepository, never()).flush();
    }

    @Test
    public void testCreateTrip_CarDeleted() throws ServiceException {
        Driver driver = new Driver();
        Car car = new Car();
        car.setDeleted(true);

        when(tripRepository.findActiveTripsByDriverAndCar(any(), any())).thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class, () -> tripService.createTrip(driver, car));
        verify(tripRepository, never()).persist(any(TripEntity.class));
        verify(tripRepository, never()).flush();
    }

    @Test
    public void testCreateTrip_CarNull() throws ServiceException {
        Driver driver = new Driver();

        when(tripRepository.findActiveTripsByDriverAndCar(any(), any())).thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class, () -> tripService.createTrip(driver, null));
        verify(tripRepository, never()).persist(any(TripEntity.class));
        verify(tripRepository, never()).flush();
    }

    @Test
    public void testCreateTrip_DriverNull() throws ServiceException {
        Car car = new Car();

        when(tripRepository.findActiveTripsByDriverAndCar(any(), any())).thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class, () -> tripService.createTrip(null, car));
        verify(tripRepository, never()).persist(any(TripEntity.class));
        verify(tripRepository, never()).flush();
    }

    @Test
    public void testCreateTripWitId_DriverNull() throws ServiceException {
        Car car = new Car();
        car.setId(UUID.randomUUID());

        assertThrows(ServiceException.class, () -> tripService.createTrip(null, car.getId()));
        verify(tripRepository, never()).persist(any(TripEntity.class));
        verify(tripRepository, never()).flush();
    }

    @Test
    public void testCreateTripWitId_CarNull() throws ServiceException {
        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());

        assertThrows(ServiceException.class, () -> tripService.createTrip(driver.getId(), null));
        verify(tripRepository, never()).persist(any(TripEntity.class));
        verify(tripRepository, never()).flush();
    }

    @Test
    public void testCreateTrip_withDriverIdAndCarId() throws Exception {
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(carService.getCar(carId)).thenReturn(car);
        doAnswer(invocation -> {
            TripEntity entity = invocation.getArgument(0);
            entity.setId(tripId); // Simulate setting the ID on persist
            return null;
        }).when(tripRepository).persist(any(TripEntity.class));
        
        Trip result = tripService.createTrip(driverId, carId);

        assertNotNull(result);
        assertEquals(tripId, result.getId());
        verify(driverService, times(1)).getDriver(driverId);
        verify(carService, times(1)).getCar(carId);
        verify(tripRepository, times(1)).persist(any(TripEntity.class));
        verify(tripRepository, times(1)).flush();
    }

    @Test
    public void testListActiveTrips() {
        when(tripRepository.listActiveTrips()).thenReturn(List.of(tripEntity));

        List<Trip> trips = tripService.listActiveTrips();

        assertNotNull(trips);
        assertEquals(1, trips.size());
        verify(tripRepository, times(1)).listActiveTrips();
    }

    @Test
    public void testListAllTrips() {
        when(tripRepository.listAll()).thenReturn(List.of(tripEntity));

        List<Trip> trips = tripService.listAllTrips();

        assertNotNull(trips);
        assertEquals(1, trips.size());
        verify(tripRepository, times(1)).listAll();
    }

    @Test
    public void testStartTrip_byId() {
        when(tripRepository.startTrip(tripId)).thenReturn(tripEntity);

        Trip result = tripService.startTrip(tripId);

        assertNotNull(result);
        assertEquals(tripId, result.getId());
        verify(tripRepository, times(1)).startTrip(tripId);
    }

    @Test
    public void testStartTrip_byDriverAndCar() {
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(carService.getCar(carId)).thenReturn(car);
        when(tripRepository.startTrip(any(), any())).thenReturn(tripEntity);

        Trip result = tripService.startTrip(driverId, carId);

        assertNotNull(result);
        assertEquals(tripId, result.getId());
        verify(driverService, times(1)).getDriver(driverId);
        verify(carService, times(1)).getCar(carId);
        verify(tripRepository, times(1)).startTrip(any(), any());
    }

    @Test
    public void testStopTrip_byId() {
        when(tripRepository.stopTrip(tripId)).thenReturn(tripEntity);

        Trip result = tripService.stopTrip(tripId);

        assertNotNull(result);
        assertEquals(tripId, result.getId());
        verify(tripRepository, times(1)).stopTrip(tripId);
    }

    @Test
    public void testStopTrip_byDriverAndCar() {
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(carService.getCar(carId)).thenReturn(car);
        when(tripRepository.stopTrip(any(), any())).thenReturn(tripEntity);

        Trip result = tripService.stopTrip(driverId, carId);

        assertNotNull(result);
        assertEquals(tripId, result.getId());
        verify(driverService, times(1)).getDriver(driverId);
        verify(carService, times(1)).getCar(carId);
        verify(tripRepository, times(1)).stopTrip(any(), any());
    }
}
