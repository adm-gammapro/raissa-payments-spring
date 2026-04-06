package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

@Data
public class ReglaRequestDto {
    Long codigo;
    String descripcion;
    String moneda;
    Double limiteInferior;
    Double limiteSuperior;

    Long codigoCliente;
}
