package com.raissa.payments.domain.dto.administrativo.response;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioPerfilResponseDto {
    private List<PerfilResponseDto> perfilesNoAsignados;
    private List<PerfilResponseDto> perfilesAsignados;
}