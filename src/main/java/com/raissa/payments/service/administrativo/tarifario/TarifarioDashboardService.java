package com.raissa.payments.service.administrativo.tarifario;


import com.raissa.payments.domain.dto.administrativo.response.tarifario.TarifarioDashboardResponse;

public interface TarifarioDashboardService {
    TarifarioDashboardResponse obtenerDashboard(Long clienteId);
}