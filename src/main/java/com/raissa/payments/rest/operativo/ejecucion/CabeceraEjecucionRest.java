package com.raissa.payments.rest.operativo.ejecucion;

import com.raissa.payments.domain.dto.operativo.ejecucion.request.CabeceraEjecucionSearchDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.CabeceraEjecucionSearchResponseDto;
import com.raissa.payments.service.operativo.ejecucion.CabeceraEjecucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cabecera-ejecucion")
@RequiredArgsConstructor
public class CabeceraEjecucionRest {
    private final CabeceraEjecucionService service;

    @PostMapping("/buscar")
    public ResponseEntity<CabeceraEjecucionSearchResponseDto> buscarPaginado(@RequestBody CabeceraEjecucionSearchDto filtros) {
        return ResponseEntity.ok(service.buscarPaginado(filtros));
    }
}