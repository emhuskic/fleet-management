package org.trg.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trg.core.domain.entity.DriverEntity;
import org.trg.core.domain.model.Driver;
import org.trg.core.repository.DriverRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
public class DriverServiceImplTest {

    @Inject
    DriverServiceImpl driverService;

    @InjectMock
    DriverRepository driverRepository;

    private DriverEntity driverEntity;
    private Driver driver;
    private UUID driverId;

    @BeforeEach
    public void setUp() {
        driverId = UUID.randomUUID();
        driverEntity = new DriverEntity();
        driverEntity.setId(driverId);
        driverEntity.setFirstName("John");
        driverEntity.setLastName("Doe");
        driverEntity.setDriversLicenseNo("D12345678");
        driverEntity.setDateOfBirth(LocalDate.of(1985, 1, 1));
        driverEntity.setDeleted(false);

        driver = Driver.fromEntity(driverEntity);
    }

    @Test
    public void testCreateDriver() {
        doAnswer(invocation -> {
            DriverEntity entity = invocation.getArgument(0);
            entity.setId(driverId); // Simulate setting the ID on persist
            return null;
        }).when(driverRepository).persist(any(DriverEntity.class));

        Driver result = driverService.createDriver(driver);

        assertNotNull(result);
        assertEquals(driverId, result.getId());
        verify(driverRepository, times(1)).persist(any(DriverEntity.class));
        verify(driverRepository, times(1)).flush();
    }

    @Test
    public void testDeleteDriver() {
        doNothing().when(driverRepository).deleteDriver(driverId);

        driverService.deleteDriver(driverId);

        verify(driverRepository, times(1)).deleteDriver(driverId);
    }

    @Test
    public void testFindDriver() {
        PanacheQuery<DriverEntity> queryMock = mock(PanacheQuery.class);
        when(driverRepository.find("id", driverId)).thenReturn(queryMock);
        when(queryMock.firstResultOptional()).thenReturn(Optional.of(driverEntity));

        Optional<Driver> result = driverService.findDriver(driverId);

        assertTrue(result.isPresent());
        assertEquals(driverId, result.get().getId());
        verify(driverRepository, times(1)).find("id", driverId);
        verify(queryMock, times(1)).firstResultOptional();
    }

    @Test
    public void testGetDriver() {
        PanacheQuery<DriverEntity> queryMock = mock(PanacheQuery.class);
        when(driverRepository.find("id", driverId)).thenReturn(queryMock);
        when(queryMock.firstResultOptional()).thenReturn(Optional.of(driverEntity));

        Driver result = driverService.getDriver(driverId);

        assertNotNull(result);
        assertEquals(driverId, result.getId());
        verify(driverRepository, times(1)).find("id", driverId);
        verify(queryMock, times(1)).firstResultOptional();
    }

    @Test
    public void testGetDriverNotFound() {
        PanacheQuery<DriverEntity> queryMock = mock(PanacheQuery.class);
        when(driverRepository.find("id", driverId)).thenReturn(queryMock);
        when(queryMock.firstResultOptional()).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            driverService.getDriver(driverId);
        });

        assertEquals(String.format("Driver with id=%s not found.", driverId), exception.getMessage());
        verify(driverRepository, times(1)).find("id", driverId);
        verify(queryMock, times(1)).firstResultOptional();
    }

    @Test
    public void testListDrivers() {
        when(driverRepository.listAll()).thenReturn(List.of(driverEntity));

        List<Driver> drivers = driverService.listDrivers();

        assertNotNull(drivers);
        assertEquals(1, drivers.size());
        verify(driverRepository, times(1)).listAll();
    }

    @Test
    public void testUpdateDriver() {
        doNothing().when(driverRepository).updateDriver(eq(driverId), any(DriverEntity.class));

        driverService.updateDriver(driverId, driver);

        verify(driverRepository, times(1)).updateDriver(eq(driverId), any(DriverEntity.class));
    }
}
