package com.raissa.payments.domain.mappers.commons;

import com.raissa.payments.domain.dto.commons.TipoDocumentoResponseDto;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.entity.commons.TipoDocumentoEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para mapear la entidad {@link TipoDocumentoEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring"
)
public abstract class TipoDocumentoMapper extends EntityMapper<TipoDocumentoEntity, String> {
    protected TipoDocumentoMapper() {
        super(TipoDocumentoEntity.class);
    }

    public abstract TipoDocumentoResponseDto entityToResponseDto(TipoDocumentoEntity entity);
}
