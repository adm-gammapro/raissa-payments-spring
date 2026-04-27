package com.raissa.payments.domain.dto.operativo.solicitud.request;

import com.raissa.payments.domain.dto.commons.InstitucionFinancieraDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlujoSolicitudConnectRequestDto {
    private Long solicitudId;
    private String usuario;
    private List<InstitucionFinancieraDto> listInstituciones;

    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
