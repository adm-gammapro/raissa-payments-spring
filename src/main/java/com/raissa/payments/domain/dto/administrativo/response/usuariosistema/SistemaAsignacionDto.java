package com.raissa.payments.domain.dto.administrativo.response.usuariosistema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SistemaAsignacionDto {
    private String id;
    private String nombre;
    private Boolean asignado;
}
