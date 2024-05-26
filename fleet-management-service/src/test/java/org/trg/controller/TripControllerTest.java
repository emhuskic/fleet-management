package org.trg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.trg.core.domain.dto.TripCreateDto;
import org.trg.core.domain.dto.TripUpdateDto;
import org.trg.core.domain.model.Trip;
import org.trg.core.exception.ServiceException;
import org.trg.core.service.TripService;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class TripControllerTest {

    @Mock
    private TripService tripService;

    @InjectMocks
    private TripController tripController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTrips() {
        final List<Trip> trips = Collections.emptyList();
        when(tripService.listAllTrips()).thenReturn(trips);

        final Response response = tripController.getAllTrips();

        assertEquals(Response.ok(trips).build().getStatus(), response.getStatus());
        verify(tripService, times(1)).listAllTrips();
    }

    @Test
    public void testGetActiveTrips() {
        final List<Trip> trips = Collections.emptyList();
        when(tripService.listActiveTrips()).thenReturn(trips);

        final Response response = tripController.getActiveTrips();

        assertEquals(Response.ok(trips).build().getStatus(), response.getStatus());
        verify(tripService, times(1)).listActiveTrips();
    }


    @Test
    public void testAddTrip_Success() throws Exception {
        // Arrange
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripCreateDto tripCreateDto = new TripCreateDto(driverId, carId);
        Trip trip = new Trip(); // Create a dummy Trip object

        when(tripService.createTrip(driverId, carId)).thenReturn(trip);

        // Act
        Response response = tripController.addTrip(tripCreateDto);

        // Assert
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(trip, response.getEntity());
        verify(tripService, times(1)).createTrip(driverId, carId);
    }

    @Test
    public void testAddTrip_ServiceException() throws Exception {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripCreateDto tripCreateDto = new TripCreateDto(driverId, carId);

        when(tripService.createTrip(driverId, carId)).thenThrow(new ServiceException("Error"));

        Response response = tripController.addTrip(tripCreateDto);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).createTrip(driverId, carId);
    }

    @Test
    public void testAddTrip_DriverNull_ServiceException() throws Exception {
        UUID carId = UUID.randomUUID();
        TripCreateDto tripCreateDto = new TripCreateDto(null, carId);

        when(tripService.createTrip(null, carId)).thenThrow(new ServiceException("Error"));

        Response response = tripController.addTrip(tripCreateDto);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).createTrip(null, carId);
    }

    @Test
    public void testAddTrip_InternalServerError() throws Exception {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripCreateDto tripCreateDto = new TripCreateDto(driverId, carId);

        when(tripService.createTrip(driverId, carId)).thenThrow(new RuntimeException("Internal Error"));

        Response response = tripController.addTrip(tripCreateDto);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).createTrip(driverId, carId);
    }

    @Test
    public void testStartTripById_Success() {
        UUID tripId = UUID.randomUUID();

        Response response = tripController.startTrip(tripId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).startTrip(tripId);
    }

    @Test
    public void testStartTripByDriverAndCar_Success() {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripUpdateDto trip = new TripUpdateDto(null, driverId, carId);

        Response response = tripController.startTrip(trip);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).startTrip(driverId, carId);
    }

    @Test
    public void testStartTripByDriverAndCar_ServiceException() {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripUpdateDto trip = new TripUpdateDto(null, driverId, carId);
        when(tripService.startTrip(driverId, carId)).thenThrow(new ServiceException("Error"));

        Response response = tripController.startTrip(trip);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).startTrip(driverId, carId);
    }

    @Test
    public void testStartTripByDriverAndCar_InternalServerError() {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripUpdateDto trip = new TripUpdateDto(null, driverId, carId);
        when(tripService.startTrip(driverId, carId)).thenThrow(new RuntimeException("Error"));

        Response response = tripController.startTrip(trip);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).startTrip(driverId, carId);
    }

    @Test
    public void testStartTrip_InvalidRequest() {
        TripUpdateDto trip = new TripUpdateDto(null, null, null);

        Response response = tripController.startTrip(trip);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(tripService, never()).startTrip(any(), any());
    }

    @Test
    public void testStopTripById_Success() {
        UUID tripId = UUID.randomUUID();

        Response response = tripController.stopTrip(tripId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).stopTrip(tripId);
    }

    @Test
    public void testStopTripByDriverAndCar_Success() {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripUpdateDto trip = new TripUpdateDto(null, driverId, carId);

        Response response = tripController.stopTrip(trip);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).stopTrip(driverId, carId);
    }

    @Test
    public void testStoptTripByDriverAndCar_ServiceException() {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripUpdateDto trip = new TripUpdateDto(null, driverId, carId);
        when(tripService.stopTrip(driverId, carId)).thenThrow(new ServiceException("Error"));

        Response response = tripController.stopTrip(trip);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).stopTrip(driverId, carId);
    }

    @Test
    public void testStopTripByDriverAndCar_InternalServerError() {
        UUID driverId = UUID.randomUUID();
        UUID carId = UUID.randomUUID();
        TripUpdateDto trip = new TripUpdateDto(null, driverId, carId);
        when(tripService.stopTrip(driverId, carId)).thenThrow(new RuntimeException("Error"));

        Response response = tripController.stopTrip(trip);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        verify(tripService, times(1)).stopTrip(driverId, carId);
    }

    @Test
    public void testStopTrip_InvalidRequest() {
        TripUpdateDto trip = new TripUpdateDto(null, null, null);

        Response response = tripController.stopTrip(trip);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(tripService, never()).stopTrip(any(), any());
    }
}
