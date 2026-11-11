package com.br.space_radar.controller;

import com.br.space_radar.dto.ImportSummaryDTO;
import com.br.space_radar.service.AsteroidService;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminImportControllerTest {
    private final AsteroidService service = mock(AsteroidService.class);

    @Test
    void refusesMissingConfiguration() {
        var controller = new AdminImportController(service, "");
        assertThrows(ResponseStatusException.class, () -> controller.importFromNasa(
                "anything", LocalDate.now(), LocalDate.now()));
        verifyNoInteractions(service);
    }

    @Test
    void refusesInvalidCredential() {
        var controller = new AdminImportController(service, "correct-secret");
        assertThrows(ResponseStatusException.class, () -> controller.importFromNasa(
                "wrong", LocalDate.now(), LocalDate.now()));
        verifyNoInteractions(service);
    }

    @Test
    void allowsConfiguredCredential() {
        var controller = new AdminImportController(service, "correct-secret");
        var date = LocalDate.of(2026, 11, 11);
        var expected = new ImportSummaryDTO(date, date, 1, 0, 2, 0);
        when(service.importFromNasa(date, date)).thenReturn(expected);
        assertEquals(expected, controller.importFromNasa("correct-secret", date, date).getBody());
    }
}

