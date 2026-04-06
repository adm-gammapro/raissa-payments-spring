package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlujoSolicitudConnectRequestDto {
    private Long solicitudId;
    private String usuario;
    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
