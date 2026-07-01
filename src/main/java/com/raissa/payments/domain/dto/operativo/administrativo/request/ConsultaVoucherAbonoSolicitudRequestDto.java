package com.raissa.payments.domain.dto.operativo.administrativo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class ConsultaVoucherAbonoSolicitudRequestDto {
    @NotNull
    private Long abonoSolicitudId;

    private String correoDestino;

    @NotNull
    private Long codigoCliente;
}