package com.raissa.payments.service.administrativo.usuario.impl;

import com.raissa.comun.exception.RaissaBasicException;
import com.raissa.comun.util.Constante;
import com.raissa.comun.util.ConstanteError;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.mappers.administrativo.UsuarioMapper;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    //Mappers
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public UsuarioResponseDto getUsuarioByUsername(String username) {
        UsuarioEntity entity = usuarioRepository.findByUsernameAndEstadoRegistro(username, Constante.ESTADO_ACTIVO)
                .orElseThrow(() -> new NotFoundException(ConstanteError.MENSAJE_ERROR_REGISTRO_NO_ENCONTRADO));

        return usuarioMapper.entityToResponseDto(entity);

    }
}
