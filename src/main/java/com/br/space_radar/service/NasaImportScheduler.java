package com.br.space_radar.service;

import com.br.space_radar.dto.ImportSummaryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@ConditionalOnProperty(name = "radar.sync.enabled", havingValue = "true")
public class NasaImportScheduler {
    private static final Logger log = LoggerFactory.getLogger(NasaImportScheduler.class);

    private final AsteroidService service;
    private final boolean syncOnStartup;
    private final Clock clock;
    private final AtomicBoolean running = new AtomicBoolean(false);

    @Autowired
    public NasaImportScheduler(AsteroidService service,
        @Value("${radar.sync.on-startup:false}") boolean syncOnStartup) {
        this(service, syncOnStartup, Clock.systemUTC());
    }

    // Additional package-visible constructor permits deterministic unit tests.
    NasaImportScheduler(AsteroidService service, boolean syncOnStartup, Clock clock) {
        this.service = service;
        this.syncOnStartup = syncOnStartup;
        this.clock = clock;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startupSync() {
        if (syncOnStartup) runSync();
    }

    @Scheduled(cron = "${radar.sync.cron:0 15 3 * * *}", zone = "UTC")
    public void scheduledSync() {
        runSync();
    }

    void runSync() {
        if (!running.compareAndSet(false, true)) {
            log.info("NASA import skipped: a synchronization is already running");
            return;
        }
        LocalDate today = LocalDate.now(clock.withZone(ZoneOffset.UTC));
        try {
            // A 7-calendar-day window: today through today + 6 days.
            ImportSummaryDTO result = service.importFromNasa(today, today.plusDays(6));
            log.info("NASA sync completed: asteroid inserts={}, updates={}, " +
                 "approach inserts={}, updates={}",
                 result.asteroidsCreated(), result.asteroidsUpdated(),
                 result.approachesCreated(), result.approachesUpdated());
        } catch (RuntimeException ex) {
            // Do not log ex or its message; HTTP client exceptions can contain
            // upstream URLs with the secret NASA API key.
            log.warn("NASA sync failed (error type: {}). Will retry at the next scheduled run.",
                 ex.getClass().getSimpleName());
        } finally {
            running.set(false);
        }
    }
}
