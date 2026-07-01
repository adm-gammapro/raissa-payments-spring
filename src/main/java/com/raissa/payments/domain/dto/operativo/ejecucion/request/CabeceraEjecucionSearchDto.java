package com.raissa.payments.domain.dto.operativo.ejecucion.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CabeceraEjecucionSearchDto extends SearchRequestDTO {
    private String estadoProcesamiento;
    private String fechaInicial;
    private String fechaFinal;
    private Long   codigoCliente;
}