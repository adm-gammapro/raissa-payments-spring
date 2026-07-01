package com.raissa.payments.domain.dto.administrativo.response.configuracionusuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionUsuarioResponseDto {
    private Long idConfiguracion;
    private Long usuarioId;
    private String username;
    private String usuarioNombre;
    private Long clienteId;
    private String clienteRazonSocial;
    private String clienteRuc;
    private String sistemaId;
    private String sistemaNombre;
    private Long perfilId;
    private String perfilDescripcion;
    private String perfilAbreviatura;
    private String estadoRegistro;
}