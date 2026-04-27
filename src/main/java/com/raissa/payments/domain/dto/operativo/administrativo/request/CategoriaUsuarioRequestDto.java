package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoriaUsuarioRequestDto {
    private Long idCategoria;
    private List<String> usernames;
    private Long codigoCliente;
}
