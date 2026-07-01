package com.raissa.payments.rest.operativo.ejecucion;

import com.raissa.payments.domain.dto.operativo.ejecucion.request.DetalleEjecucionSearchDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.DetalleEjecucionSearchResponseDto;
import com.raissa.payments.service.operativo.ejecucion.DetalleEjecucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/detalle-ejecucion")
@RequiredArgsConstructor
public class DetalleEjecucionRest {
    private final DetalleEjecucionService service;

    @PostMapping("/buscar")
    public ResponseEntity<DetalleEjecucionSearchResponseDto> buscarPaginado(@RequestBody DetalleEjecucionSearchDto filtros) {
        return ResponseEntity.ok(service.buscarPaginado(filtros));
    }
}