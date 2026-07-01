package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CuentaOrdenanteConnectRequestDto {
    private Long codigo;
    private String usuarioOrdenante;
    private String passwordOrdenante;
    private String numeroCuentaOrdenante;
    private String monedaCuentaOrdenante;
    private String tipoDocumentoOrdenante;
    private String documentoOrdenante;
    private String nombreOrdenante;
    private String apellidoPaternoOrdenante;
    private String apellidoMaternoOrdenante;

    // Auditoría
    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
