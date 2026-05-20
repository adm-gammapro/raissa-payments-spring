package com.raissa.payments.domain.dto.administrativo.response.perfilcliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilClientesTransferDto {
    private Long perfilId;
    private String descripcion;
    private String abreviatura;
    private String nombreComercial;
    private List<ClienteAsignacionDto> clientesDisponibles;
    private List<ClienteAsignacionDto> clientesAsignados;
}