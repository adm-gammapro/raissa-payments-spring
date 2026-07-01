package com.raissa.payments.rest.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.configuracionusuario.ConfiguracionUsuarioRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.ConfiguracionUsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.OpcionesConfiguracionDto;
import com.raissa.payments.service.administrativo.usuario.ConfiguracionUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/configuracion-usuario")
@RequiredArgsConstructor
@Slf4j
public class ConfiguracionUsuarioRest {
    private final ConfiguracionUsuarioService configuracionUsuarioService;

    @GetMapping("/opciones/{usuarioId}")
    @Operation(summary = "Obtener opciones de configuración",
            description = "Retorna clientes, sistemas y perfiles disponibles para el usuario")
    public ResponseEntity<OpcionesConfiguracionDto> obtenerOpcionesConfiguracion(@PathVariable Long usuarioId) {
        OpcionesConfiguracionDto response = configuracionUsuarioService.obtenerOpcionesConfiguracion(usuarioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar/{usuarioId}")
    @Operation(summary = "Listar configuraciones por usuario",
            description = "Retorna todas las configuraciones de un usuario")
    public ResponseEntity<List<ConfiguracionUsuarioResponseDto>> listarConfiguracionesPorUsuario(@PathVariable Long usuarioId) {
        List<ConfiguracionUsuarioResponseDto> response = configuracionUsuarioService
                .listarConfiguracionesPorUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/guardar")
    @Operation(summary = "Guardar configuración",
            description = "Crea o actualiza una configuración para el usuario")
    public ResponseEntity<ConfiguracionUsuarioResponseDto> guardarConfiguracion(@Valid @RequestBody ConfiguracionUsuarioRequestDto transfer,
                                                                                HttpServletRequest request) {
        ConfiguracionUsuarioResponseDto response = configuracionUsuarioService.guardarConfiguracion(transfer, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/eliminar/{configuracionId}")
    @Operation(summary = "Eliminar configuración",
            description = "Elimina (borrado lógico) una configuración existente")
    public ResponseEntity<Void> eliminarConfiguracion(@PathVariable Long configuracionId,
                                                      HttpServletRequest request) {
        configuracionUsuarioService.eliminarConfiguracion(configuracionId, request);
        return ResponseEntity.ok().build();
    }
}