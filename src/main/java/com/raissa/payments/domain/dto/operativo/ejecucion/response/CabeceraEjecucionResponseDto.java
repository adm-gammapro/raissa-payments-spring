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
public class CabeceraEjecucionResponseDto {
    private Long id;
    private Integer codigoJob;
    private String codigoSistema;
    private Integer codigoCliente;
    private String fechaInicioProceso;
    private String fechaFinProceso;
    private String estadoProcesamiento;
    private Integer registrosTotales;
    private Integer registrosProcesados;
    private Integer registrosErroneos;
    private Integer registrosPendientes;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}