package com.raissa.payments.domain.mappers.administrativo;

import com.raissa.payments.domain.dto.administrativo.response.ModuloResponseDto;
import com.raissa.payments.domain.entity.administrativo.ModuloEntity;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.mappers.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para mapear la entidad {@link ModuloEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring",
        uses = {
                MapperUtil.class
        }
)
public abstract class ModuloMapper extends EntityMapper<ModuloEntity, Long> {
    protected ModuloMapper() {
        super(ModuloEntity.class);
    }

    /**
     * Carga los datos de un entity en un dto
     *
     * @param entity datos del entity
     * @return {@link ModuloResponseDto}
     */
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    public abstract ModuloResponseDto entityToResponseDto(ModuloEntity entity);
}
