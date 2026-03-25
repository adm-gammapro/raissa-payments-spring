package com.raissa.payments.domain.dto.operativo.solicitud.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CargaSolicitudResponseDto {
    Long solicitudId;
    int cargosCreados;
    int abonosCreados;
    int observacionesRegistradas;
    String estadoSolicitud;
}