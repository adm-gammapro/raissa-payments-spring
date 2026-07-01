package com.raissa.payments.domain.dto.administrativo.response.tarifario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarifarioDashboardResponse {
    private String sistema;
    private String plan;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer diasRestantes;
    private Integer usuariosContratados;
    private Integer usuariosUtilizados;
    private Integer usuariosDisponibles;
    private Integer consumosContratados;
    private Integer consumosUtilizados;
    private Integer consumosDisponibles;
    private BigDecimal porcentajeConsumo;
    private Boolean periodoPrueba;
    private Boolean suscripcionActiva;
}