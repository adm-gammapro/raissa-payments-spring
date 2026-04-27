package com.raissa.payments.domain.dto.operativo.solicitud.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;

import java.util.List;

@Data
public class SolicitudSearchConnectDto extends SearchRequestDTO {
    private String usuario;
    private String fechaInicial;
    private String fechaFinal;
    private String codigo;
    private List<String> estadoSolicitud;
}