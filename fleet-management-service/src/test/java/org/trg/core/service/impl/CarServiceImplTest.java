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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trg.core.domain.entity.CarEntity;
import org.trg.core.domain.model.Car;
import org.trg.core.repository.CarRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
public class CarServiceImplTest {

    @Inject
    CarServiceImpl carService;

    @InjectMock
    CarRepository carRepository;

    private CarEntity carEntity;
    private Car car;
    private UUID carId;

    @BeforeEach
    public void setUp() {
        carId = UUID.randomUUID();
        carEntity = new CarEntity();
        carEntity.setId(carId);
        carEntity.setModel("Model S");
        carEntity.setColor("Red");
        carEntity.setManufacturer("Tesla");
        carEntity.setLicensePlate("ABC-123");

        car = Car.fromEntity(carEntity);
    }

    @Test
    public void testListAllCars() {
        when(carRepository.listAll()).thenReturn(List.of(carEntity));

        List<Car> cars = carService.listAllCars();

        assertNotNull(cars);
        assertEquals(1, cars.size());
        verify(carRepository, times(1)).listAll();
    }

    @Test
    public void testAddCar() {
        doAnswer(invocation -> {
            CarEntity entity = invocation.getArgument(0);
            entity.setId(carId); // Simulate setting the ID on persist
            return null;
        }).when(carRepository).persist(any(CarEntity.class));

        Car result = carService.addCar(car);

        assertNotNull(result);
        assertEquals(carId, result.getId());
        verify(carRepository, times(1)).persist(any(CarEntity.class));
        verify(carRepository, times(1)).flush();
    }

    @Test
    public void testFindCar() {
        PanacheQuery<CarEntity> queryMock = mock(PanacheQuery.class);
        when(carRepository.find("id", carId)).thenReturn(queryMock);
        when(queryMock.firstResultOptional()).thenReturn(Optional.of(carEntity));

        Optional<Car> result = carService.findCar(carId);

        assertTrue(result.isPresent());
        assertEquals(carId, result.get().getId());
        verify(carRepository, times(1)).find("id", carId);
    }

    @Test
    public void testGetCar() {
        PanacheQuery<CarEntity> queryMock = mock(PanacheQuery.class);
        when(carRepository.find("id", carId)).thenReturn(queryMock);
        when(queryMock.firstResultOptional()).thenReturn(Optional.of(carEntity));

        Car result = carService.getCar(carId);

        assertNotNull(result);
        assertEquals(carId, result.getId());
        verify(carRepository, times(1)).find("id", carId);
    }

    @Test
    public void testGetCarNotFound() {
        PanacheQuery<CarEntity> queryMock = mock(PanacheQuery.class);
        when(carRepository.find("id", carId)).thenReturn(queryMock);
        when(queryMock.firstResultOptional()).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            carService.getCar(carId);
        });

        assertEquals(String.format("Car with id=%s not found.", carId), exception.getMessage());
        verify(carRepository, times(1)).find("id", carId);
    }

    @Test
    public void testUpdateCar() {
        doNothing().when(carRepository).updateCar(eq(carId), any(CarEntity.class));

        carService.updateCar(carId, car);

        verify(carRepository, times(1)).updateCar(eq(carId), any(CarEntity.class));
    }

    @Test
    public void testDeleteCar() {
        doNothing().when(carRepository).deleteCar(carId);

        carService.deleteCar(carId);

        verify(carRepository, times(1)).deleteCar(carId);
    }
}
