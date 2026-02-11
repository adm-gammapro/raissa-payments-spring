package com.raissa.payments.domain.mappers.administrativo;

import com.raissa.payments.domain.dto.administrativo.response.OpcionResponseDto;
import com.raissa.payments.domain.entity.administrativo.OpcionEntity;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.mappers.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para mapear la entidad {@link OpcionEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring",
        uses = {
                MapperUtil.class
        }
)
public abstract class OpcionMapper extends EntityMapper<OpcionEntity, Long> {
    protected OpcionMapper() {
        super(OpcionEntity.class);
    }

    /**
     * Carga los datos de un entity en un dto
     *
     * @param entity datos del entity
     * @return {@link OpcionResponseDto}
     */
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "codigoModulo", source = "modulo.codigo")
    @Mapping(target = "descripcionModulo", source = "modulo.nombreModulo")
    public abstract OpcionResponseDto entityToResponseDto(OpcionEntity entity);
}
