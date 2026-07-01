package com.raissa.payments.domain.dto.operativo.administrativo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstanciaPagoResponse {
    private String codigoOperacion;

    private String bancoOrigen;

    private String cuentaOrigen;

    private String destinatario;

    private String destino;

    private String entidadDestino;

    private String moneda;

    private String monto;

    private String fecha;
}
