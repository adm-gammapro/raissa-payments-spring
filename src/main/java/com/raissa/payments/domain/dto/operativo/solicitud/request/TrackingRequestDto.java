package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

@Data
public class TrackingRequestDto {
    private Long idSolicitud;
    private Long codigoCliente;
}
