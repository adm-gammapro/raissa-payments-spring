package com.raissa.payments.domain.dto.administrativo.response.tarifario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarConsumoResponse {
    private Long consumoId;
    private Boolean facturable;
    private BigDecimal importe;
    private Integer consumosUtilizados;
    private Integer consumosDisponibles;
}