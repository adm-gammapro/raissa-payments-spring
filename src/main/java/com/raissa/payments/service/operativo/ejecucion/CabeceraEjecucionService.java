package com.raissa.payments.service.operativo.ejecucion;

import com.raissa.payments.domain.dto.operativo.ejecucion.request.CabeceraEjecucionSearchDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.CabeceraEjecucionResponseDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.CabeceraEjecucionSearchResponseDto;

public interface CabeceraEjecucionService {
    CabeceraEjecucionResponseDto buscarPorId(Long id, Long codigoCliente);

    CabeceraEjecucionSearchResponseDto buscarPaginado(CabeceraEjecucionSearchDto filtros);
}
