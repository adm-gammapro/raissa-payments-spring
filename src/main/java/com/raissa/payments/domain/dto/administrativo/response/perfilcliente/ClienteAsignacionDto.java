package com.raissa.payments.domain.dto.administrativo.response.perfilcliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteAsignacionDto {
    private Long id;
    private String razonSocial;
    private String ruc;
    private Boolean asignado;
}