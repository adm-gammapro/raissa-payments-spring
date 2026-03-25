package com.raissa.payments.domain.mappers.commons;

import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.entity.commons.ClienteDataSourceEntity;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.mappers.administrativo.ClienteMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                ClienteMapper.class,
                MapperUtil.class,
        }
)
public abstract class ClienteDataSourceMapper extends EntityMapper<ClienteDataSourceEntity, Long> {
    protected ClienteDataSourceMapper() {
        super(ClienteDataSourceEntity.class);
    }

    /**
     * Carga los datos de un entity en un dto
     *
     * @param entity datos de la entidad
     * @return {@link ClienteDataSourceResponseDto}
     */
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract ClienteDataSourceResponseDto entityToResponseDto(ClienteDataSourceEntity entity);
}
