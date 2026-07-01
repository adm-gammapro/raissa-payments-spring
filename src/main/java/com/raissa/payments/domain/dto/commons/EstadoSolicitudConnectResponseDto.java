package com.raissa.payments.domain.dto.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoSolicitudConnectResponseDto {
    private String codigo;
    private String descripcion;
}