package com.raissa.payments.domain.dto.operativo.solicitud.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class SolicitudSearchConnectDto extends SearchRequestDTO {
    private List<String> usuarios;
    private String fechaInicial;
    private String fechaFinal;
    private String codigo;
    private List<String> estadoSolicitud;
    private String usuarioActual;
    private String proceso;//'cargar' 'gestionar' | 'validar' | 'autorizar' | 'ejecutar';
}