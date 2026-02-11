package com.raissa.payments.service.administrativo.cliente.impl;

import com.raissa.payments.domain.dto.administrativo.response.ClienteResponseDto;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.mappers.administrativo.ClienteMapper;
import com.raissa.payments.domain.repository.administrativo.ClienteRepository;
import com.raissa.payments.service.administrativo.cliente.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    //Repositorios
    private final ClienteRepository clienteRepository;

    //Mappers
    private final ClienteMapper clienteMapper;

    public ClienteResponseDto obtenerCliente(Long codigoCliente) {
        ClienteEntity entity = clienteRepository.findById(codigoCliente).orElseThrow(
                () -> new EntityNotFoundException("Cliente con código " + codigoCliente + " no encontrado")
        );

        return clienteMapper.entityToResponseDto(entity);
    }
}
