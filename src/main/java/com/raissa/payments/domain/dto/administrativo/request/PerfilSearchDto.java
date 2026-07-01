package com.raissa.payments.domain.dto.administrativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PerfilSearchDto extends SearchRequestDTO {
    private String descripcion;
    private String abreviatura;
    private String  estadoRegistro;
    private Long codigoCliente;
}