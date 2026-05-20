package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LineaCargaRequestDto {
    String tipo;
    String cuenta;
    String codigoEntidadFinanciera;
    String moneda;
    BigDecimal monto;
    String beneficiario;
    String tipoDocBeneficiario;
    String nroDocBeneficiario;
    String mismoTitular;
}