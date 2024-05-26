package org.trg.controller;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.trg.core.domain.model.Car;
import org.trg.core.service.CarService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarController {

    @Inject
    private CarService carService;

    @GET
    @Path("/{id}")
    public Response findCar(@PathParam("id") final UUID id) {
        final Optional<Car> car = carService.findCar(id);
        if (car.isPresent()) {
            return Response.ok(car.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/details/{id}")
    public Response getCar(@PathParam("id") final UUID id) {
        try {
            final Car car = carService.getCar(id);
            return Response.ok(car).build();
        } catch (final NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (final Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    public Response getAllCars() {
        List<Car> cars = carService.listAllCars();
        return Response.ok(cars).build();
    }

    @Transactional
    @POST
    public Response addCar(final Car car) {
        final Car createdCar = carService.addCar(car);
        return Response.status(Response.Status.CREATED).entity(createdCar).build();
    }

    @Transactional
    @PUT
    @Path("/{id}")
    public Response updateCar(@PathParam("id") final UUID id, final Car car) {
        carService.updateCar(id, car);
        return Response.ok().build();
    }

    @Transactional
    @DELETE
    @Path("/{id}")
    public Response deleteCar(@PathParam("id") final UUID id) {
        carService.deleteCar(id);
        return Response.noContent().build();
    }
}
