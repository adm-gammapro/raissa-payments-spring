package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlujoSolicitudRequestDto {
    Long idSolicitud;
    String flujo;
    Long codigoCliente;
}
