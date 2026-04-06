package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfiguracionReglaConnectRequestDto {
    private Long codigo;
    private Long codigoRegla;
    private Long codigoCategoria;
    private String codigoModo;
    private String predeterminado;
    private Integer prioridad;

    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
