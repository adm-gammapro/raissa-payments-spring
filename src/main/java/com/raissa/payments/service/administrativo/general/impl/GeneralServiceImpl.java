package com.raissa.payments.service.administrativo.general.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;
import com.raissa.payments.domain.entity.commons.InstitucionFinancieraEntity;
import com.raissa.payments.domain.mappers.commons.InstitucionFinancieraMapper;
import com.raissa.payments.domain.repository.commons.InstitucionFinancieraRepository;
import com.raissa.payments.service.administrativo.general.GeneralService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GeneralServiceImpl extends AbstractRaissaPaymentsService implements GeneralService {
    //Repositorios
    private final InstitucionFinancieraRepository institucionFinancieraRepository;

    //Mappers
    private final InstitucionFinancieraMapper institucionFinancieraMapper;

    public List<InstitucionFinancieraResponseDto> listarInstitucionFinanciera() {
        List<InstitucionFinancieraEntity> listInstitucionFinanciera = institucionFinancieraRepository.findAll();

        return listInstitucionFinanciera.stream()
                .map(institucionFinancieraMapper::entityToResponseDto)
                .toList();
    }
}