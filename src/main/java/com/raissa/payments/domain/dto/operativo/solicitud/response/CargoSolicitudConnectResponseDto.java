package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CargoSolicitudConnectResponseDto {
    private Long id;
    private Long solicitudId;
    private String cuentaOrigen;
    private String codigoEntidadFinanciera;
    private String moneda;
    private BigDecimal montoCargo;
    private BigDecimal montoTotalAbonos;
    private String estadoValidacion;
    private String estadoEjecucion;
    private List<AbonoSolicitudConnectResponseDto> abonos;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
