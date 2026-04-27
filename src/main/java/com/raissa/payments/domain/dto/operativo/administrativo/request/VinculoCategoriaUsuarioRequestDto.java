package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VinculoCategoriaUsuarioRequestDto {
    private Long idCategoria;

    private Long codigoCliente;
}
