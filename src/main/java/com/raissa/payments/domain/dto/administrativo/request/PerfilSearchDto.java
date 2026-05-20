package com.raissa.payments.domain.dto.administrativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PerfilSearchDto extends SearchRequestDTO {
    private String estadoRegistro;
    private String  nombrePerfil;
    private Long codigoCliente;
}