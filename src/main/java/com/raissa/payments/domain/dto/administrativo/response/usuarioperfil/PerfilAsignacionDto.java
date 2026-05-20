package com.raissa.payments.domain.dto.administrativo.response.usuarioperfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilAsignacionDto {
    private Long id;
    private String descripcion;
    private String abreviatura;
    private String nombreComercial;
    private Boolean asignado;
}
