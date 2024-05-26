package org.trg.interfaces.messages;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.trg.core.domain.Heartbeat;
import org.trg.core.services.PenaltyService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

@QuarkusTest
public class HeartbeatListenerTest {

    @Mock
    PenaltyService penaltyServiceMock;

    @InjectMocks
    HeartbeatListener heartbeatListener;

    public HeartbeatListenerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsume_ProcessPenalty() throws Exception {
        final Heartbeat heartbeat = new Heartbeat(UUID.randomUUID(), UUID.randomUUID(), 77.5, 34.5, 44.5);

        when(penaltyServiceMock.processPenalty(heartbeat)).thenReturn(Uni.createFrom().nullItem());

        final Uni<Void> result = heartbeatListener.consume(heartbeat);

        verify(penaltyServiceMock).processPenalty(heartbeat);

        assert result != null;
    }

    @Test
    public void testConsume_ProcessPenalty_NullHeartbeat() throws Exception {
        final Heartbeat heartbeat = null;
        assertDoesNotThrow(() -> heartbeatListener.consume(heartbeat));
    }

    @Test
    public void testConsume_ProcessPenalty_ErrorHandling() {
        final Heartbeat heartbeat = new Heartbeat(UUID.randomUUID(), UUID.randomUUID(), 77.5, 34.5, 44.5);

        when(penaltyServiceMock.processPenalty(heartbeat)).thenThrow(new RuntimeException("Error processing penalty"));

        assertThrows(Exception.class, () -> heartbeatListener.consume(heartbeat));
    }

}
