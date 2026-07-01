package com.raissa.payments.domain.dto.administrativo.response.configuracionusuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SistemaOpcionDto {
    private String id;
    private String nombre;
}