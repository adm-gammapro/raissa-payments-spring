package com.raissa.payments.domain.dto.administrativo.request.tarifario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarConsumoRequest {
    private String sistemaId;
    private String codigoApi;
    private Long clienteId;
    private String username;
    private Boolean exitoso;
    private String codigoResultado;
    private String observacion;
}