package com.raissa.payments.domain.dto.administrativo.response.configuracionusuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpcionesConfiguracionDto {
    private List<ClienteOpcionDto> clientes;
    private List<SistemaOpcionDto> sistemas;
    private List<PerfilOpcionDto> perfiles;
}
