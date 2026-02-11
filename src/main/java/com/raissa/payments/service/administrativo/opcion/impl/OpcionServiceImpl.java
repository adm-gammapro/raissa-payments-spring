package com.raissa.payments.service.administrativo.opcion.impl;

import com.raissa.payments.domain.dto.administrativo.response.MenuUsuarioResponseDto;
import com.raissa.payments.domain.mappers.administrativo.ModuloMapper;
import com.raissa.payments.domain.mappers.administrativo.OpcionMapper;
import com.raissa.payments.domain.repository.administrativo.ModuloRespository;
import com.raissa.payments.domain.repository.administrativo.OpcionRepository;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.opcion.OpcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpcionServiceImpl implements OpcionService {
    @Value("${raissa.sistema.codigo}")
    private String codigoSistema;

    //Repositories
    private final OpcionRepository opcionRepository;
    private final ModuloRespository moduloRespository;

    //Mappers
    private final OpcionMapper opcionMapper;
    private final ModuloMapper moduloMapper;

    public MenuUsuarioResponseDto getOpcionesXUsuario(String username, Long idEmpresa) {
        var modulos = moduloRespository.listModulos(username, idEmpresa);
        if (modulos.isEmpty()) {
            throw new NotFoundException("Módulos no encontrados");
        }

        var menuPadres = opcionRepository.listMenuPadres(username, idEmpresa, codigoSistema);
        if (menuPadres.isEmpty()) {
            throw new NotFoundException("Opciones padre no encontradas");
        }

        var menuBase = opcionRepository.listMenuBase(username, idEmpresa, codigoSistema);
        if (menuBase.isEmpty()) {
            throw new NotFoundException("Opciones base no encontrados");
        }

        return MenuUsuarioResponseDto.builder()
                .listModulo(modulos.stream()
                        .map(moduloMapper::entityToResponseDto)
                        .toList())
                .listOpcionPadres(menuPadres.stream()
                        .map(opcionMapper::entityToResponseDto)
                        .toList())
                .listOpcionBase(menuBase.stream()
                        .map(opcionMapper::entityToResponseDto)
                        .toList())
                .build();
    }
}
