package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

@Data
public class ObservacionRequestDto {
    private Long solicitudId;
    private String descripcion;
    private String tipoObservacion;
    private String eventoObservacion;
    private String usuarioObservacion;

    private Long codigoCliente;
}
