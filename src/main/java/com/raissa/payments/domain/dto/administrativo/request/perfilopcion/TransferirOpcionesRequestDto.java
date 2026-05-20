package com.raissa.payments.domain.dto.administrativo.request.perfilopcion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferirOpcionesRequestDto {
    private Long perfilId;
    private List<Long> opcionesIdsAsignar;
    private List<Long> opcionesIdsDesasignar;
}