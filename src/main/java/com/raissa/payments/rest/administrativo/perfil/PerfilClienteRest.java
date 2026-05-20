package com.raissa.payments.rest.administrativo.perfil;

import com.raissa.payments.domain.dto.administrativo.request.perfilcliente.TransferirClientesPerfilRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilcliente.PerfilClientesTransferDto;
import com.raissa.payments.service.administrativo.perfil.PerfilClienteService;
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
@RequestMapping("/api/perfil-clientes")
@RequiredArgsConstructor
@Slf4j
public class PerfilClienteRest {
    private final PerfilClienteService perfilClienteService;

    @GetMapping("/transfer/{perfilId}")
    @Operation(summary = "Obtener clientes para transferencia",
            description = "Retorna clientes disponibles y asignados para el perfil")
    public ResponseEntity<PerfilClientesTransferDto> obtenerClientesParaTransferencia(
            @PathVariable Long perfilId) {
        PerfilClientesTransferDto response = perfilClienteService.obtenerClientesParaTransferencia(perfilId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transferir clientes",
            description = "Asigna y/o desasigna clientes a un perfil en una sola operación")
    public ResponseEntity<Void> transferirClientes(@Valid @RequestBody TransferirClientesPerfilRequestDto transfer,
                                                   HttpServletRequest request) {
        perfilClienteService.transferirClientes(transfer, request);
        return ResponseEntity.ok().build();
    }
}