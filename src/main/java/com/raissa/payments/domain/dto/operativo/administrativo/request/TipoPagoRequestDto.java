package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

@Data
public class TipoPagoRequestDto {
    Long codigo;
    String descripcion;

    Long codigoCliente;
}
