package com.raissa.payments.domain.dto.administrativo.response.perfilopcion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilOpcionesTransferDto {
    private Long perfilId;
    private String descripcion;
    private String abreviatura;
    private String nombreComercial;
    private List<OpcionAsignacionDto> opcionesDisponibles;
    private List<OpcionAsignacionDto> opcionesAsignadas;
}
