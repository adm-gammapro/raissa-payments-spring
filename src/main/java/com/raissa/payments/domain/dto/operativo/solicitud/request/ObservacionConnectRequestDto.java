package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ObservacionConnectRequestDto {
    private Long solicitudId;
    private String descripcion;
    private String tipoObservacion;
    private String eventoObservacion;
    private String usuarioObservacion;
    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
