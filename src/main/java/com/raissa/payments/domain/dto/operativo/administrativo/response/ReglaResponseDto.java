package com.raissa.payments.domain.dto.operativo.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class ReglaResponseDto {
    Long codigo;
    String descripcion;
    String moneda;
    Double limiteInferior;
    Double limiteSuperior;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
