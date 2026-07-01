package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

@Data
public class CuentaOrdenanteRequestDto {
    private Long codigo;
    private String usuarioOrdenante;
    private String passwordOrdenante;
    private String numeroCuentaOrdenante;
    private String monedaCuentaOrdenante;
    private String tipoDocumentoOrdenante;
    private String documentoOrdenante;
    private String nombreOrdenante;
    private String apellidoPaternoOrdenante;
    private String apellidoMaternoOrdenante;

    private Long codigoCliente;
}