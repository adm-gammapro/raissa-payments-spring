package com.raissa.payments.domain.dto.operativo.ejecucion.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleEjecucionResponseDto {
    private Long id;
    private Long codigoCabeceraEjecucion;
    private Long codigoCuenta;
    private String numeroCuenta;
    private String codigoEntidadFinanciera;
    private String nombreEntidadFinanciera;
    private String monedaCuenta;
    private String descripcionMoneda;
    private String estadoProcesamiento;
    private String detalleEjecucion;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}