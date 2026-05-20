package com.raissa.payments.domain.mappers.commons;

import com.raissa.payments.domain.dto.commons.TipoDocumentoResponseDto;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.entity.commons.TipoDocumentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para mapear la entidad {@link TipoDocumentoEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring",
        uses = {
                MapperUtil.class,
                TipoDocumentoMapper.class
        }
)
public abstract class TipoDocumentoMapper extends EntityMapper<TipoDocumentoEntity, String> {
    protected TipoDocumentoMapper() {
        super(TipoDocumentoEntity.class);
    }

    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract TipoDocumentoResponseDto entityToResponseDto(TipoDocumentoEntity entity);
}
