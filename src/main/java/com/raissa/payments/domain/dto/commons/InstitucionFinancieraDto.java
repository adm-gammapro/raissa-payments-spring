package com.raissa.payments.domain.dto.commons;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstitucionFinancieraDto {
    private String codigo;
    private String codigoSbs;
}
