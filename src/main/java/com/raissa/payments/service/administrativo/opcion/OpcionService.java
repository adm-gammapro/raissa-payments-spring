package com.raissa.payments.service.administrativo.opcion;

import com.raissa.payments.domain.dto.administrativo.response.MenuUsuarioResponseDto;

public interface OpcionService {
    MenuUsuarioResponseDto getOpcionesXUsuario(String username, Long idEmpresa);
}