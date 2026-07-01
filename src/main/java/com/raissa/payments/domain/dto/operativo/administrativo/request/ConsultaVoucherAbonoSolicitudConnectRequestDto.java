package com.raissa.payments.domain.dto.operativo.administrativo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class ConsultaVoucherAbonoSolicitudConnectRequestDto {
    @NotNull
    private Long abonoSolicitudId;
}
