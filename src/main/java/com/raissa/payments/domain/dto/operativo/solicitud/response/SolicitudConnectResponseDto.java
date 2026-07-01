package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.util.List;

@Data
public class SolicitudConnectResponseDto {
    private Long id;
    private String fechaCarga;
    private String usuarioCarga;
    private Integer cantidadOrdenes;
    private Boolean enProcesamiento;
    private String estadoSolicitud;
    private List<String> usuariosAutorizacion;
    private List<CargoSolicitudConnectResponseDto> cargos;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}