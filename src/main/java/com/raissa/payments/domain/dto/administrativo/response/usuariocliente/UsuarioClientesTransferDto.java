package com.raissa.payments.domain.dto.administrativo.response.usuariocliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioClientesTransferDto {
    private Long usuarioId;
    private String username;
    private String nombreCompleto;
    private List<ClienteAsignacionDto> clientesDisponibles;
    private List<ClienteAsignacionDto> clientesAsignados;
}
