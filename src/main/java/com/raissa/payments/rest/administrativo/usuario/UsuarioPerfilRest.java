package com.raissa.payments.rest.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.usuarioperfil.TransferirPerfilesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuarioperfil.UsuarioPerfilesTransferDto;
import com.raissa.payments.service.administrativo.usuario.UsuarioPerfilService;
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
@RequestMapping("/api/usuario-perfiles")
@RequiredArgsConstructor
@Slf4j
public class UsuarioPerfilRest {
    private final UsuarioPerfilService usuarioPerfilService;

    @GetMapping("/transfer/{usuarioId}")
    @Operation(summary = "Obtener perfiles para transferencia",
            description = "Retorna perfiles disponibles y asignados para el usuario")
    public ResponseEntity<UsuarioPerfilesTransferDto> obtenerPerfilesParaTransferencia(@PathVariable Long usuarioId) {
        UsuarioPerfilesTransferDto response = usuarioPerfilService.obtenerPerfilesParaTransferencia(usuarioId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transferir perfiles",
            description = "Asigna y/o desasigna perfiles a un usuario en una sola operación")
    public ResponseEntity<Void> transferirPerfiles(@Valid @RequestBody TransferirPerfilesRequestDto transfer,
                                                   HttpServletRequest request) {
        usuarioPerfilService.transferirPerfiles(transfer, request);
        return ResponseEntity.ok().build();
    }
}