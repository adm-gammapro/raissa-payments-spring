package com.raissa.payments.domain.dto.administrativo.request.usuariosistema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferirSistemasRequestDto {
    private Long usuarioId;
    private List<String> sistemasIdsAsignar;
    private List<String> sistemasIdsDesasignar;
}
