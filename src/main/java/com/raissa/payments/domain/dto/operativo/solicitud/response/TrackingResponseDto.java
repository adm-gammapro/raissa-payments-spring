package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class TrackingResponseDto {
    private String fecha;
    private String usuario;
    private String evento;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
