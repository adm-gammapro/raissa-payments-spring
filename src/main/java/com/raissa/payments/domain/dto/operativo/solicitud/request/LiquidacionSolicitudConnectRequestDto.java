package com.raissa.payments.domain.dto.operativo.solicitud.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class LiquidacionSolicitudConnectRequestDto {
    @NotNull
    private Long solicitudId;
}
