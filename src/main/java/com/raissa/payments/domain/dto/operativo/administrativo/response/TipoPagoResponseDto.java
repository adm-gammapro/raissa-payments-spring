package com.raissa.payments.domain.dto.operativo.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class TipoPagoResponseDto {
    Long codigo;
    String descripcion;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}