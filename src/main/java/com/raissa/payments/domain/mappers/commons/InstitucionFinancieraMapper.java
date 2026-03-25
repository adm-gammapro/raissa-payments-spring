package com.raissa.payments.domain.mappers.commons;

import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.entity.commons.InstitucionFinancieraEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para mapear la entidad {@link InstitucionFinancieraEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring"
)
public abstract class InstitucionFinancieraMapper extends EntityMapper<InstitucionFinancieraEntity, String> {
    protected InstitucionFinancieraMapper() {
        super(InstitucionFinancieraEntity.class);
    }

    public abstract InstitucionFinancieraResponseDto entityToResponseDto(InstitucionFinancieraEntity entity);
}
