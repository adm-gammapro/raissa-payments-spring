package com.raissa.payments.domain.dto.operativo.administrativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReglaSearchDto extends SearchRequestDTO {
    String descripcion;
    String moneda;
    String estadoRegistro;
    Long codigoCliente;
}
