package com.raissa.payments.domain.dto.operativo.solicitud.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CargaSolicitudResponseDto {
    private List<Long> idsSolicitudes;
    private int totalCargos;
    private int totalAbonos;
    private int totalObservaciones;
}