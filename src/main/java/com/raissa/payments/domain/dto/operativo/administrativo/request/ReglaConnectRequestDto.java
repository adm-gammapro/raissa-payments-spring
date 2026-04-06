package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReglaConnectRequestDto {
    Long codigo;
    String descripcion;
    String moneda;
    Double limiteInferior;
    Double limiteSuperior;

    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
