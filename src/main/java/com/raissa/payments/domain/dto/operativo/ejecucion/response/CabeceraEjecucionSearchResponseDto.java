package com.raissa.payments.domain.dto.operativo.ejecucion.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CabeceraEjecucionSearchResponseDto {
    private int    totalPages;
    private long   totalElements;
    private int    number;
    private int    size;
    private List<CabeceraEjecucionResponseDto> content;
}
