package com.raissa.payments.service.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;

public interface UsuarioService {
    UsuarioResponseDto getUsuarioByUsername(String username);
}
