package com.raissa.payments.domain.dto.administrativo.response.usuariosistema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSistemasTransferDto {
    private Long usuarioId;
    private String username;
    private String nombreCompleto;
    private List<SistemaAsignacionDto> sistemasDisponibles;
    private List<SistemaAsignacionDto> sistemasAsignados;
}
