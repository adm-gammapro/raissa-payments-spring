package com.raissa.payments.domain.dto.operativo.solicitud.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiquidacionSolicitudResponseDto {
    private Long idSolicitud;
    private BigDecimal totalLiquidacion;
    private BigDecimal cargo;
    private BigDecimal totalCobros;
    private BigDecimal totalComisiones;
    private BigDecimal totalImpuestos;
    private BigDecimal totalComisionesOrigen;
    private BigDecimal totalComisionesDestino;
    private String moneda;
    List<DetalleLiquidacionSolicitud> detalle;
}
