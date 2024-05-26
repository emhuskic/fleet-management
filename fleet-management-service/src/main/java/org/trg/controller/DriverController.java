package org.trg.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.trg.core.domain.model.Driver;
import org.trg.core.service.DriverService;

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

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverController {

    @Inject
    private DriverService driverService;

    @GET
    public List<Driver> listDrivers() {
        return driverService.listDrivers();
    }

    @GET
    @Path("/{id}")
    public Response findDriver(@PathParam("id") final UUID id) {
        final Optional<Driver> driver = driverService.findDriver(id);
        if (driver.isPresent()) {
            return Response.ok(driver.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/details/{id}")
    public Response getDriver(@PathParam("id") final UUID id) {
        try {
            final Driver driver = driverService.getDriver(id);
            return Response.ok(driver).build();
        } catch (final NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (final Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @POST
    public Response createDriver(final Driver driver) {
        final Driver createdDriver = driverService.createDriver(driver);
        return Response.status(Response.Status.CREATED).entity(createdDriver).build();
    }

    @Transactional
    @PUT
    @Path("/{id}")
    public Response updateDriver(@PathParam("id") final UUID id, final Driver driver) {
        driverService.updateDriver(id, driver);
        return Response.ok().build();
    }

    @Transactional
    @DELETE
    @Path("/{id}")
    public Response deleteDriver(@PathParam("id") final UUID id) {
        driverService.deleteDriver(id);
        return Response.noContent().build();
    }

}
