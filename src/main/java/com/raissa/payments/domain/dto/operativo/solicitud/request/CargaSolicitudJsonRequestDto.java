package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CargaSolicitudJsonRequestDto {
    Long codigoCliente;
    String usuarioCarga;
    String tipoCarga;
    List<LineaCargaRequestDto> lineas;

    LocalDateTime fechaAuditoria;
    String usuarioAuditoria;
    String terminalAuditoria;
    String ipAuditoria;
}