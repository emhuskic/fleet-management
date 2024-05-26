package org.trg.controller;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.trg.core.domain.dto.TripCreateDto;
import org.trg.core.domain.dto.TripUpdateDto;
import org.trg.core.domain.model.Trip;
import org.trg.core.exception.ServiceException;
import org.trg.core.service.TripService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/trips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TripController {
    
    @Inject
    private TripService tripService;

    @GET
    public Response getAllTrips() {
        List<Trip> trips = tripService.listAllTrips();
        return Response.ok(trips).build();
    }


    @GET
    @Path("/active")
    public Response getActiveTrips() {
        List<Trip> trips = tripService.listActiveTrips();
        return Response.ok(trips).build();
    }

    @Transactional
    @POST
    public Response addTrip(@RequestBody TripCreateDto tripCreateDto) {
        try {
            final Trip trip = tripService.createTrip(tripCreateDto.driverId(), tripCreateDto.carId());
            return Response.status(Response.Status.CREATED).entity(trip).build();
        } catch (ServiceException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @POST
    @Path("/start/{id}")
    public Response startTrip(@PathParam("id") final UUID id) {
        tripService.startTrip(id);
        return Response.status(Response.Status.OK).build();
    }

    @Transactional
    @POST
    @Path("/start")
    public Response startTrip(@RequestBody TripUpdateDto trip) {
        try {
            if (trip.id() != null) {
                tripService.startTrip(trip.id());
            } else if (trip.driverId() != null && trip.carId() != null) {
                tripService.startTrip(trip.driverId(), trip.carId());
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).build();
        } catch (final ServiceException exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (final Exception exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @POST
    @Path("/stop/{id}")
    public Response stopTrip(@PathParam("id") final UUID id) {
        tripService.stopTrip(id);
        return Response.status(Response.Status.OK).build();
    }

    @Transactional
    @POST
    @Path("/stop")
    public Response stopTrip(@RequestBody final TripUpdateDto trip) {
        try {
            if (trip.id() != null) {
                tripService.stopTrip(trip.id());
            } else if (trip.driverId() != null && trip.carId() != null) {
                tripService.stopTrip(trip.driverId(), trip.carId());
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).build();
        } catch (final ServiceException exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (final Exception exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
