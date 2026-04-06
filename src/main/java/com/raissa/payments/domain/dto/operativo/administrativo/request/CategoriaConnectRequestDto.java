package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoriaConnectRequestDto {
    Long codigo;
    String descripcion;

    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
