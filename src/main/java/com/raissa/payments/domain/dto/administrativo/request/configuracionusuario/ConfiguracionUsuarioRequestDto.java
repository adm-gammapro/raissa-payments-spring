package com.raissa.payments.domain.dto.administrativo.request.configuracionusuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionUsuarioRequestDto {
    private Long idConfiguracion;
    private Long usuarioId;
    private Long clienteId;
    private String sistemaId;
    private Long perfilId;
}