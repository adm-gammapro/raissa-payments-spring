package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObservacionFlujoSolicitudRequestDto extends FlujoSolicitudRequestDto {
    private String descripcionObservacion;

    private String eventoObservacion;

    private String usuarioObservacion;
}
