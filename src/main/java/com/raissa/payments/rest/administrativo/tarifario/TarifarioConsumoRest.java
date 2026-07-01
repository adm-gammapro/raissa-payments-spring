package com.raissa.payments.rest.administrativo.tarifario;

import com.raissa.payments.domain.dto.administrativo.request.tarifario.RegistrarConsumoRequest;
import com.raissa.payments.service.administrativo.tarifario.TarifarioConsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/tarifario")
public class TarifarioConsumoRest {
    private final TarifarioConsumoService consumoService;

    @PostMapping("/consumo")
    public ResponseEntity<Void> registrarConsumo(@RequestHeader("X-KEY-ID") String keyId,
                                                 @RequestHeader("X-INTERNAL-TOKEN") String token,
                                                 @RequestBody RegistrarConsumoRequest request) {

        consumoService.registrarConsumo(keyId, token, request);

        return ResponseEntity.ok().build();
    }
}