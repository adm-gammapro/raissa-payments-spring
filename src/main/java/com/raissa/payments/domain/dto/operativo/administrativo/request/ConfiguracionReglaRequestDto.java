package com.raissa.payments.domain.dto.operativo.administrativo.request;

import lombok.Data;

@Data
public class ConfiguracionReglaRequestDto {
    private Long codigo;
    private Long codigoRegla;
    private Long codigoCategoria;
    private String codigoModo;
    private String predeterminado;
    private Integer prioridad;
    private Long codigoCliente;
}