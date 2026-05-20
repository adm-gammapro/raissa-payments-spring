package com.raissa.payments.domain.dto.administrativo.request.perfilcliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferirClientesPerfilRequestDto {
    private Long perfilId;
    private List<Long> clientesIdsAsignar;
    private List<Long> clientesIdsDesasignar;
}