package com.raissa.payments.rest.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.UsuarioRequestDto;
import com.raissa.payments.domain.dto.administrativo.request.UsuarioResetPasswordRequetDto;
import com.raissa.payments.domain.dto.administrativo.request.UsuarioSearchDto;
import com.raissa.payments.domain.dto.administrativo.response.MenuUsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioSearchResponseDto;
import com.raissa.payments.service.administrativo.opcion.OpcionService;
import com.raissa.payments.service.administrativo.usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/registrar-usuario")
    public ResponseEntity<UsuarioResponseDto> createUsuario(@Valid @RequestBody UsuarioRequestDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.registrar(requestDto, request));
    }

    @PostMapping("/actualizar-usuario")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(@Valid @RequestBody UsuarioRequestDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(requestDto, request));
    }

    @PostMapping("/eliminar-usuario/{codigo}")
    public ResponseEntity<UsuarioResponseDto> deleteUsuario(@PathVariable Long codigo, HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.eliminar(codigo, request));
    }

    @PostMapping("/listarUsuarios")
    public ResponseEntity<UsuarioSearchResponseDto> listarUsuariosPage(@RequestBody(required = false) UsuarioSearchDto usuarioSearch) {
        return ResponseEntity.ok(usuarioService.getAllUsuario(usuarioSearch));
    }

    @GetMapping("/obtenerUsuario")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuario(@RequestParam(name = "codigoUsuario", required = false) Long codigoUsuario) {
        return ResponseEntity.ok(usuarioService.getUsuario(codigoUsuario));

    }

    @PostMapping("/actualizar-perfil-usuario")
    public ResponseEntity<UsuarioResponseDto> updatePerfilUsuario(@Valid @RequestBody UsuarioRequestDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarDatosUsuarioEnPerfil(requestDto, request));
    }

    @PostMapping("/resetear-password")
    public ResponseEntity<UsuarioResponseDto> resetearPassword(@RequestBody UsuarioResetPasswordRequetDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.resetearPassword(requestDto, request));
    }

    /*@GetMapping("/listarUsuarioPerfil")
    public ResponseEntity<UsuarioPerfilResponseDto> listarPerfilesAndUsuario(@RequestParam(name = "idUsuario", required = false) Long idUsuario,
                                                                             @RequestParam(name = "idEmpresa", required = false) Long idEmpresa) {
        return ResponseEntity.ok(usuarioService.getUsuarioPerfil(idUsuario, idEmpresa));
    }*/

    /*@PostMapping("/vincular-usuario-perfil")
    public ResponseEntity<Void> vincularUsuarioPerfil(@RequestBody UsuarioPerfilRequestDto requestDto,
                                                      HttpServletRequest request) {
        usuarioService.vincularPerfiles(requestDto.getCodigoUsuario(), requestDto.getCodigoPerfil(), request);

        return ResponseEntity.ok().build();
    }*/

    /*@PostMapping("/desvincular-usuario-perfil")
    public ResponseEntity<Void> desvincularUsuarioPerfil(@RequestBody UsuarioPerfilRequestDto usuarioPerfilRequest,
                                                         HttpServletRequest request) {
        usuarioService.desvincularPerfiles(usuarioPerfilRequest.getCodigoUsuario(),
                usuarioPerfilRequest.getCodigoPerfil(),
                request);

        return ResponseEntity.ok().build();
    }*/

    /*@PostMapping("/actualizar-perfil-usuario")
    public ResponseEntity<UsuarioResponseDto> updatePerfilUsuario(@Valid @RequestBody UsuarioRequestDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(requestDto, request));
    }*/
}