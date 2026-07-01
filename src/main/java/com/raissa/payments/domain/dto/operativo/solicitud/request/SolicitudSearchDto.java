package com.raissa.payments.domain.dto.operativo.solicitud.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SolicitudSearchDto extends SearchRequestDTO {
    private String usuario;
    private String fechaInicial;
    private String fechaFinal;
    private String codigo;
    private List<String> estadoSolicitud;
    private Long codigoCliente;
    private String usuarioActual;
    private String proceso;//'cargar' 'gestionar' | 'validar' | 'autorizar' | 'ejecutar';
}