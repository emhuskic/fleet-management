package org.trg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.trg.core.domain.model.Car;
import org.trg.core.service.CarService;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findCar_ExistingCar_ReturnsCar() {
        UUID carId = UUID.randomUUID();
        Car car = new Car();
        car.setId(carId);
        when(carService.findCar(carId)).thenReturn(Optional.of(car));

        var response = carController.findCar(carId);

        assertEquals(200, response.getStatus());
        assertEquals(car, response.getEntity());
    }

    @Test
    void findCar_NonExistingCar_ReturnsNotFound() {
        UUID carId = UUID.randomUUID();
        when(carService.findCar(carId)).thenReturn(Optional.empty());

        var response = carController.findCar(carId);

        assertEquals(404, response.getStatus());
    }

    @Test
    void getCar_ExistingCar_ReturnsCar() {
        UUID carId = UUID.randomUUID();
        Car car = new Car();
        car.setId(carId);
        when(carService.getCar(carId)).thenReturn(car);

        var response = carController.getCar(carId);

        assertEquals(200, response.getStatus());
        assertEquals(car, response.getEntity());
    }

    @Test
    public void testGetCar_NotFound() {
        final UUID carId = UUID.randomUUID();
        when(carService.getCar(carId)).thenThrow(new NotFoundException("Car not found"));

        final Response response = carController.getCar(carId);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Car not found", response.getEntity());
        verify(carService, times(1)).getCar(carId);
    }

    @Test
    public void testGetCar_InternalServerError() {
        final UUID carId = UUID.randomUUID();
        when(carService.getCar(carId)).thenThrow(new RuntimeException("Internal Server Error"));

        final Response response = carController.getCar(carId);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        verify(carService, times(1)).getCar(carId);
    }

    @Test
    void getAllCars_ReturnsListOfCars() {
        List<Car> cars = Collections.singletonList(new Car());
        when(carService.listAllCars()).thenReturn(cars);

        var response = carController.getAllCars();

        assertEquals(200, response.getStatus());
        assertEquals(cars, response.getEntity());
    }

    @Test
    void addCar_CarAdded_ReturnsCreated() {
        Car car = new Car();
        when(carService.addCar(any())).thenReturn(car);

        var response = carController.addCar(car);

        assertEquals(201, response.getStatus());
        assertEquals(car, response.getEntity());
    }

    @Test
    void updateCar_CarUpdated_ReturnsOk() {
        UUID carId = UUID.randomUUID();
        Car car = new Car();
        doNothing().when(carService).updateCar(eq(carId), any());

        var response = carController.updateCar(carId, car);

        assertEquals(200, response.getStatus());
    }

    @Test
    void deleteCar_CarDeleted_ReturnsNoContent() {
        UUID carId = UUID.randomUUID();

        var response = carController.deleteCar(carId);

        assertEquals(204, response.getStatus());
    }
}
