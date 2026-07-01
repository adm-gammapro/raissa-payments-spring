package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.util.List;

@Data
public class SolicitudResponseDto {
    private Long id;
    private String fechaCarga;
    private String usuarioCarga;
    private String nombreUsuarioCarga;
    private Integer cantidadOrdenes;
    private Boolean enProcesamiento;
    private String estadoSolicitud;
    private String nombresUsuariosAutorizacion;
    private List<CargoSolicitudResponseDto> cargos;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}