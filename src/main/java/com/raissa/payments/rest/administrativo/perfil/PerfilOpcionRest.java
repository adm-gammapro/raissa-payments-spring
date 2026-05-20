package com.raissa.payments.rest.administrativo.perfil;

import com.raissa.payments.domain.dto.administrativo.request.perfilopcion.TransferirOpcionesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilopcion.PerfilOpcionesTransferDto;
import com.raissa.payments.service.administrativo.perfil.PerfilOpcionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perfil-opciones")
@RequiredArgsConstructor
@Slf4j
public class PerfilOpcionRest {
    private final PerfilOpcionService perfilOpcionService;

    @GetMapping("/transfer/{perfilId}")
    @Operation(summary = "Obtener opciones para transferencia",
            description = "Retorna opciones disponibles y asignadas para el perfil")
    public ResponseEntity<PerfilOpcionesTransferDto> obtenerOpcionesParaTransferencia(@PathVariable Long perfilId) {
        PerfilOpcionesTransferDto response = perfilOpcionService.obtenerOpcionesParaTransferencia(perfilId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transferir opciones",
            description = "Asigna y/o desasigna opciones a un perfil en una sola operación")
    public ResponseEntity<Void> transferirOpciones(@Valid @RequestBody TransferirOpcionesRequestDto transfer,
                                                   HttpServletRequest request) {
        perfilOpcionService.transferirOpciones(transfer, request);
        return ResponseEntity.ok().build();
    }
}