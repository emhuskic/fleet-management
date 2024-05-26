package org.trg.interfaces.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.trg.core.domain.Penalty;
import org.trg.core.services.RedisClient;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@QuarkusTest
public class PenaltyControllerTest {

    @Mock
    private RedisClient redisClientMock;

    public PenaltyControllerTest() {
        openMocks(this);
    }

    @Test
    public void testGetPenalty_ExistingPenalty() {
        when(redisClientMock.get(anyString())).thenReturn(Uni.createFrom().item(10.0));

        PenaltyController controller = new PenaltyController();
        controller.redisClient = redisClientMock;

        Response response = controller.getPenalty("driver123");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final Penalty penalty = (Penalty) response.getEntity();
        assertEquals("driver123", penalty.driverId());
        assertEquals(10.0, penalty.fine());
    }

    @Test
    public void testGetPenalty_NonExistingPenalty() {
        when(redisClientMock.get(anyString())).thenReturn(null);

        PenaltyController controller = new PenaltyController();
        controller.redisClient = redisClientMock;

        final Response response = controller.getPenalty("driver123");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetPenalty_InternalServerError() {
        when(redisClientMock.get(anyString())).thenThrow(new RuntimeException("Internal Server Error"));

        PenaltyController controller = new PenaltyController();
        controller.redisClient = redisClientMock;

        final Response response = controller.getPenalty("driver123");

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}
