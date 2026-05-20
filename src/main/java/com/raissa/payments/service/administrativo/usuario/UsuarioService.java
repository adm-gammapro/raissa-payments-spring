package com.raissa.payments.service.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.UsuarioRequestDto;
import com.raissa.payments.domain.dto.administrativo.request.UsuarioResetPasswordRequetDto;
import com.raissa.payments.domain.dto.administrativo.request.UsuarioSearchDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioSearchResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UsuarioService {
    UsuarioResponseDto getUsuarioByUsername(String username);

    UsuarioSearchResponseDto getAllUsuario(UsuarioSearchDto usuarioSearch);

    UsuarioResponseDto registrar(UsuarioRequestDto requestDto, HttpServletRequest request);

    UsuarioResponseDto actualizar(UsuarioRequestDto requestDto, HttpServletRequest request);

    UsuarioResponseDto eliminar(Long id, HttpServletRequest request);

    UsuarioResponseDto getUsuario(Long id);

    UsuarioResponseDto actualizarDatosUsuarioEnPerfil(UsuarioRequestDto requestDto, HttpServletRequest request);

    UsuarioResponseDto resetearPassword(UsuarioResetPasswordRequetDto req, HttpServletRequest request);
}
