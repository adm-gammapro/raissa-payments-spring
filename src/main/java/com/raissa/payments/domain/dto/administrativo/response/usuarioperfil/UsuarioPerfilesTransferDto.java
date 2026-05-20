package com.raissa.payments.domain.dto.administrativo.response.usuarioperfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilesTransferDto {
    private Long usuarioId;
    private String username;
    private String nombreCompleto;
    private List<PerfilAsignacionDto> perfilesDisponibles;
    private List<PerfilAsignacionDto> perfilesAsignados;
}
