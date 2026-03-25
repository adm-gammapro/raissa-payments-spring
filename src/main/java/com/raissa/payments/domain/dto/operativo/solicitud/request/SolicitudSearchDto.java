package com.raissa.payments.domain.dto.operativo.solicitud.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SolicitudSearchDto extends SearchRequestDTO {
    private String usuario;
    private String fecha;
    private String codigo;
    private String estadoSolicitud;
    private Long codigoCliente;
}