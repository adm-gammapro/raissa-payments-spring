package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SolicitudResponseDto {
    private Long id;
    private LocalDateTime fechaCarga;
    private String usuarioCarga;
    private Integer cantidadOrdenes;
    private String estadoSolicitud;
    private List<CargoSolicitudResponseDto> cargos;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}