package org.trg.interfaces.api;

import org.trg.core.domain.Penalty;
import org.trg.core.services.RedisClient;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class PenaltyController {

    @Inject
    RedisClient redisClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/penalty/{driverId}")
    public Response getPenalty(@PathParam("driverId") final String driverId) {
        try {
            final Uni<Double> fine = redisClient.get(driverId);
            if (fine == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(new Penalty(driverId, fine.await().indefinitely())).build();
        } catch (final Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
