package com.br.space_radar.service;

import com.br.space_radar.dto.ImportSummaryDTO;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NasaImportSchedulerTest {
    @Test
    void syncsInclusiveWeekInUtc() {
        var service = mock(AsteroidService.class);
        var clock = Clock.fixed(Instant.parse("2026-11-11T23:50:00Z"), ZoneOffset.UTC);
        var scheduler = new NasaImportScheduler(service, true, clock);
        var start = LocalDate.of(2026, 11, 11);
        var end = start.plusDays(6);
        when(service.importFromNasa(start, end))
             .thenReturn(new ImportSummaryDTO(start, end, 2, 0, 3, 0));
        scheduler.startupSync();
        verify(service).importFromNasa(start, end);
    }

    @Test
    void startupIsOptional() {
        var service = mock(AsteroidService.class);
        var scheduler = new NasaImportScheduler(service, false,
                Clock.fixed(Instant.parse("2026-11-11T00:00:00Z"), ZoneOffset.UTC));
        scheduler.startupSync();
        verifyNoInteractions(service);
    }

    @Test
    void upstreamErrorsDoNotCrashScheduler() {
        var service = mock(AsteroidService.class);
        var clock = Clock.fixed(Instant.parse("2026-11-11T00:00:00Z"), ZoneOffset.UTC);
        var scheduler = new NasaImportScheduler(service, false, clock);
        when(service.importFromNasa(any(), any()))
            .thenThrow(new RuntimeException("test failure"));
        assertDoesNotThrow(scheduler::scheduledSync);
    }
}
