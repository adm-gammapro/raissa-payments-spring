package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

@Data
public class CategoriaRequestDto {
    Long codigo;
    String descripcion;

    Long codigoCliente;
}
