package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LineaCargaRequestDto {
    String tipo;
    String cuenta;
    String codigoEntidadFinanciera;
    String moneda;
    BigDecimal monto;
    String beneficiario;
}