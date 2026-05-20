package com.raissa.payments.domain.dto.administrativo.request.usuarioperfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferirPerfilesRequestDto {
    private Long usuarioId;
    private List<Long> perfilesIdsAsignar;
    private List<Long> perfilesIdsDesasignar;
}
