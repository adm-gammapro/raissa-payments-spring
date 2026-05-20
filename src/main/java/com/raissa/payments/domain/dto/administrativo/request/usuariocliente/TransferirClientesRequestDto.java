package com.raissa.payments.domain.dto.administrativo.request.usuariocliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferirClientesRequestDto {
    private Long usuarioId;
    private List<Long> clientesIdsAsignar;
    private List<Long> clientesIdsDesasignar;
}
