package com.raissa.payments.service.administrativo.perfil;

import com.raissa.payments.domain.dto.administrativo.request.PerfilRequestDto;
import com.raissa.payments.domain.dto.administrativo.request.PerfilSearchDto;
import com.raissa.payments.domain.dto.administrativo.response.PerfilResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.PerfilSearchResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface PerfilService {
    PerfilResponseDto registrar(PerfilRequestDto requestDto, HttpServletRequest request);

    PerfilResponseDto actualizar(PerfilRequestDto requestDto, HttpServletRequest request);

    PerfilResponseDto eliminar(Long id, HttpServletRequest request);

    PerfilResponseDto getPerfil(Long id);

    PerfilSearchResponseDto getPerfilesPage(PerfilSearchDto perfilSearch);
}
