package com.raissa.payments.domain.dto.administrativo.response.perfilopcion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpcionAsignacionDto {
    private Long id;
    private String descripcion;
    private String ruta;
    private String icono;
    private Long opcionPadre;
    private Integer numeroOrden;
    private Boolean asignado;
}