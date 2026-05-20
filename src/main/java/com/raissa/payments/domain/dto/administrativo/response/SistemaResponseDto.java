package com.raissa.payments.domain.dto.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;

public class SistemaResponseDto {
    private String codigo;
    private String nombre;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}