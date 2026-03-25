package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

@Data
public class SolicitudSearchConnectDto {
    private String usuario;
    private String fecha;
    private String codigo;
    private String estadoSolicitud;
}