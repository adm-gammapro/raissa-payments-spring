package com.raissa.payments.domain.dto.administrativo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuUsuarioResponseDto {
    List<ModuloResponseDto> listModulo;
    List<OpcionResponseDto> listOpcionPadres;
    List<OpcionResponseDto> listOpcionBase;
}
