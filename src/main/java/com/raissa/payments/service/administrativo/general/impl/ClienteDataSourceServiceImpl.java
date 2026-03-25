package com.raissa.payments.service.administrativo.general.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.entity.commons.ClienteDataSourceEntity;
import com.raissa.payments.domain.mappers.commons.ClienteDataSourceMapper;
import com.raissa.payments.domain.repository.commons.ClienteDataSourceRepository;
import com.raissa.payments.service.administrativo.general.ClienteDataSourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteDataSourceServiceImpl implements ClienteDataSourceService {
    //Repositorios
    private final ClienteDataSourceRepository clienteDataSourceRepository;

    //Mappers
    private final ClienteDataSourceMapper clienteDataSourceMapper;

    public ClienteDataSourceResponseDto getDataSource(Long codigoCliente) {
        ClienteDataSourceEntity entity = clienteDataSourceRepository.findByClienteCodigoAndEstadoRegistro(codigoCliente, EstadoRegistroEnum.VIGENTE.getValor());
        return clienteDataSourceMapper.entityToResponseDto(entity);
    }
}
