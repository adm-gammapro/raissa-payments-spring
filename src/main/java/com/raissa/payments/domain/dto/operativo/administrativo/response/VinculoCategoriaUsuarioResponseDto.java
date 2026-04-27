package com.raissa.payments.domain.dto.operativo.administrativo.response;

import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VinculoCategoriaUsuarioResponseDto {
    private Long idCategoria;
    private List<UsuarioResponseDto> usuariosDisponibles;
    private List<UsuarioResponseDto> usuariosVinculados;
}
