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
import org.trg.core.domain.model.Driver;
import org.trg.core.service.DriverService;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class DriverControllerTest {

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverController driverController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listDrivers_ReturnsListOfDrivers() {
        final List<Driver> drivers = Collections.singletonList(new Driver());
        when(driverService.listDrivers()).thenReturn(drivers);

        var response = driverController.listDrivers();

        assertEquals(1, response.size());
    }

    @Test
    void findDriver_ExistingDriver_ReturnsDriver() {
        final UUID driverId = UUID.randomUUID();
        final Driver driver = new Driver();
        driver.setId(driverId);
        when(driverService.findDriver(driverId)).thenReturn(Optional.of(driver));

        var response = driverController.findDriver(driverId);

        assertEquals(200, response.getStatus());
        assertEquals(driver, response.getEntity());
    }

    @Test
    void findDriver_NonExistingDriver_ReturnsNotFound() {
        final UUID driverId = UUID.randomUUID();
        when(driverService.findDriver(driverId)).thenReturn(Optional.empty());

        var response = driverController.findDriver(driverId);

        assertEquals(404, response.getStatus());
    }

    @Test
    void getDriver_ExistingDriver_ReturnsDriver() {
        final UUID driverId = UUID.randomUUID();
        final Driver driver = new Driver();
        driver.setId(driverId);
        when(driverService.getDriver(driverId)).thenReturn(driver);

        var response = driverController.getDriver(driverId);

        assertEquals(200, response.getStatus());
        assertEquals(driver, response.getEntity());
    }

    @Test
    void getDriver_NonExistingDriver_ReturnsNotFound() {
        final UUID driverId = UUID.randomUUID();
        final NotFoundException exception = new NotFoundException("Driver not found");
        when(driverService.getDriver(driverId)).thenThrow(exception);

        var response = driverController.getDriver(driverId);

        assertEquals(404, response.getStatus());
        assertEquals("Driver not found", response.getEntity());
    }

    @Test
    void getDriver_NonExistingDriver_ReturnsInternalServerError() {
        final UUID driverId = UUID.randomUUID();
        final RuntimeException exception = new RuntimeException("Error");
        when(driverService.getDriver(driverId)).thenThrow(exception);

        var response = driverController.getDriver(driverId);

        assertEquals(500, response.getStatus());
    }

    @Test
    void createDriver_ValidDriver_ReturnsCreatedResponse() {
        final Driver driver = new Driver();
        when(driverService.createDriver(driver)).thenReturn(driver);

        var response = driverController.createDriver(driver);

        assertEquals(201, response.getStatus());
        assertEquals(driver, response.getEntity());
    }

    @Test
    void updateDriver_ValidDriver_ReturnsOkResponse() {
        final UUID driverId = UUID.randomUUID();
        final Driver driver = new Driver();
        doNothing().when(driverService).updateDriver(eq(driverId), any(Driver.class));

        var response = driverController.updateDriver(driverId, driver);

        assertEquals(200, response.getStatus());
    }

    @Test
    void deleteDriver_ValidDriver_ReturnsNoContentResponse() {
        final UUID driverId = UUID.randomUUID();

        var response = driverController.deleteDriver(driverId);

        assertEquals(204, response.getStatus());
        verify(driverService, times(1)).deleteDriver(driverId);
    }
}
