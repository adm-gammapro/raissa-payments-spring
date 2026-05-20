package com.raissa.payments.rest.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.usuariocliente.TransferirClientesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariocliente.UsuarioClientesTransferDto;
import com.raissa.payments.service.administrativo.usuario.UsuarioClienteService;
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
@RequestMapping("/api/usuario-clientes")
@RequiredArgsConstructor
@Slf4j
public class UsuarioClienteRest {
    private final UsuarioClienteService usuarioClienteService;

    @GetMapping("/transfer/{usuarioId}")
    @Operation(summary = "Obtener clientes para transferencia",
            description = "Retorna clientes disponibles y asignados para el usuario")
    public ResponseEntity<UsuarioClientesTransferDto> obtenerClientesParaTransferencia(
            @PathVariable Long usuarioId) {
        UsuarioClientesTransferDto response = usuarioClienteService.obtenerClientesParaTransferencia(usuarioId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transferir clientes",
            description = "Asigna y/o desasigna clientes a un usuario en una sola operación")
    public ResponseEntity<Void> transferirClientes(@Valid @RequestBody TransferirClientesRequestDto transfer,
                                                   HttpServletRequest request) {
        usuarioClienteService.transferirClientes(transfer, request);
        return ResponseEntity.ok().build();
    }
}