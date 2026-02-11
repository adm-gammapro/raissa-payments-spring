package com.raissa.payments.domain.mappers.commons;

import com.raissa.payments.domain.dto.commons.TipoClienteResponseDto;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.entity.commons.TipoClienteEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para mapear la entidad {@link TipoClienteEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring"
)
public abstract class TipoClienteMapper extends EntityMapper<TipoClienteEntity, String> {
    protected TipoClienteMapper() {
        super(TipoClienteEntity.class);
    }

    public abstract TipoClienteResponseDto entityToResponseDto(TipoClienteEntity entity);
}
