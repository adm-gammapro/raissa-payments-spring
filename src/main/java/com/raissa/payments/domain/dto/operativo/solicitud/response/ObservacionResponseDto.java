package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class ObservacionResponseDto {
    private Long id;
    private String descripcion;
    private String tipoObservacion;
    private String eventoObservacion;
    private String usuarioObservacion;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
    private Long solicitudId;
}
