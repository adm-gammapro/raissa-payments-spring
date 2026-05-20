package com.raissa.payments.rest.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.usuariosistema.TransferirSistemasRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariosistema.UsuarioSistemasTransferDto;
import com.raissa.payments.service.administrativo.usuario.UsuarioSistemaService;
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
@RequestMapping("/api/usuario-sistemas")
@RequiredArgsConstructor
@Slf4j
public class UsuarioSistemaRest {
    private final UsuarioSistemaService usuarioSistemaService;

    @GetMapping("/transfer/{usuarioId}")
    @Operation(summary = "Obtener sistemas para transferencia",
            description = "Retorna sistemas disponibles y asignados para el usuario")
    public ResponseEntity<UsuarioSistemasTransferDto> obtenerSistemasParaTransferencia(
            @PathVariable Long usuarioId) {
        UsuarioSistemasTransferDto response = usuarioSistemaService.obtenerSistemasParaTransferencia(usuarioId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transferir")
    @Operation(summary = "Transferir sistemas",
            description = "Asigna y/o desasigna sistemas a un usuario en una sola operación")
    public ResponseEntity<Void> transferirSistemas(@Valid @RequestBody TransferirSistemasRequestDto transfer,
                                                   HttpServletRequest request) {
        usuarioSistemaService.transferirSistemas(transfer, request);
        return ResponseEntity.ok().build();
    }
}
