package com.raissa.payments.domain.dto.operativo.ejecucion.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleEjecucionSearchResponseDto {
    private int    totalPages;
    private long   totalElements;
    private int    number;
    private int    size;
    private List<DetalleEjecucionResponseDto> content;
}