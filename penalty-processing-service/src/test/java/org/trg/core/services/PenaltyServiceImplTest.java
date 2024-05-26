package org.trg.core.services;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.trg.core.domain.Heartbeat;
import org.trg.core.services.impl.FineCalculationServiceImpl;
import org.trg.core.services.impl.PenaltyServiceImpl;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class PenaltyServiceImplTest {

    @InjectMock
    private RedisClient redisClientMock;

    @InjectMock
    private FineCalculationServiceImpl fineCalculationService;

    @Inject
    private PenaltyServiceImpl penaltyService;

    @Test
    public void testProcessPenalty() {
        final Heartbeat heartbeat = new Heartbeat(UUID.randomUUID(), UUID.randomUUID(), 77.5, 34.5, 44.5);
        double fine = 10.0; 
        when(fineCalculationService.calculateFine(heartbeat.speed())).thenReturn(fine);
        when(redisClientMock.increment(heartbeat.driverId(), fine)).thenReturn(Uni.createFrom().nullItem());

        final Uni<Void> result = penaltyService.processPenalty(heartbeat);

        verify(fineCalculationService).calculateFine(heartbeat.speed());
        verify(redisClientMock).increment(heartbeat.driverId(), fine);

        assert result != null;
    }
}
