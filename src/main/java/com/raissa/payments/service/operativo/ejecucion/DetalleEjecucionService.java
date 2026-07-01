package com.raissa.payments.service.operativo.ejecucion;

import com.raissa.payments.domain.dto.operativo.ejecucion.request.DetalleEjecucionSearchDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.DetalleEjecucionResponseDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.DetalleEjecucionSearchResponseDto;

public interface DetalleEjecucionService {
    DetalleEjecucionResponseDto buscarPorId(Long id, Long codigoCliente);

    DetalleEjecucionSearchResponseDto buscarPaginado(DetalleEjecucionSearchDto filtros);
}
