package com.raissa.payments.rest.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.response.MenuUsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.service.administrativo.opcion.OpcionService;
import com.raissa.payments.service.administrativo.usuario.UsuarioService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioRest {
    //Services
    private final UsuarioService usuarioService;
    private final OpcionService opcionService;

    @GetMapping("/obtenerUsuarioByUsername")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuarioByUsername(@RequestParam @NotBlank String username) {
            return ResponseEntity.ok(usuarioService.getUsuarioByUsername(username));
    }

    @GetMapping("/listarOpcionesUsuario")
    public ResponseEntity<MenuUsuarioResponseDto> listarOpcionesXUsuario(@RequestParam(name = "usuario", required = false) String usuario,
                                                                         @RequestParam(name = "idEmpresa", required = false) Long idEmpresa) {
        return ResponseEntity.ok(opcionService.getOpcionesXUsuario(usuario, idEmpresa));
    }
}