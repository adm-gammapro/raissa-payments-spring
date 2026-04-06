package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

@Data
public class FlujoSolicitudRequestDto {
    Long idSolicitud;
    String flujo;
    Long codigoCliente;
}
