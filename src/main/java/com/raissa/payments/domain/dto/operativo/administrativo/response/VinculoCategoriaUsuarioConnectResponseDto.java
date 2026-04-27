package com.raissa.payments.domain.dto.operativo.administrativo.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VinculoCategoriaUsuarioConnectResponseDto {
    private Long idCategoria;
    private List<String> usuariosDisponibles;
    private List<String> usuariosVinculados;
}