package com.raissa.payments.service.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.configuracionusuario.ConfiguracionUsuarioRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.ConfiguracionUsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.OpcionesConfiguracionDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ConfiguracionUsuarioService {
    OpcionesConfiguracionDto obtenerOpcionesConfiguracion(Long usuarioId);

    List<ConfiguracionUsuarioResponseDto> listarConfiguracionesPorUsuario(Long usuarioId);

    ConfiguracionUsuarioResponseDto guardarConfiguracion(ConfiguracionUsuarioRequestDto transfer, HttpServletRequest request);

    void eliminarConfiguracion(Long configuracionId, HttpServletRequest request);
}
