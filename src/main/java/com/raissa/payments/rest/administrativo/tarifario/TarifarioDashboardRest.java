package com.raissa.payments.rest.administrativo.tarifario;

import com.raissa.payments.domain.dto.administrativo.response.tarifario.TarifarioDashboardResponse;
import com.raissa.payments.service.administrativo.tarifario.TarifarioDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tarifario")
public class TarifarioDashboardRest {
    private final TarifarioDashboardService dashboardService;

    @GetMapping("/dashboard/{clienteId}")
    public ResponseEntity<TarifarioDashboardResponse>
    obtenerDashboard(@PathVariable Long clienteId) {
        return ResponseEntity.ok(dashboardService.obtenerDashboard(clienteId));
    }
}