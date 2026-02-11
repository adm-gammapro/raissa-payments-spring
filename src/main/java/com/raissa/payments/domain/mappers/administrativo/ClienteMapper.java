package com.raissa.payments.domain.mappers.administrativo;

import com.raissa.payments.domain.dto.administrativo.response.ClienteResponseDto;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.mappers.commons.MapperUtil;
import com.raissa.payments.domain.mappers.commons.TipoClienteMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                TipoClienteMapper.class,
                MapperUtil.class,
        }
)
public abstract class ClienteMapper extends EntityMapper<ClienteEntity, Long> {
    protected ClienteMapper() {
        super(ClienteEntity.class);
    }

    /**
     * Carga los datos de un entity en un dto
     *
     * @param entity datos de la entidad
     * @return {@link ClienteResponseDto}
     */
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract ClienteResponseDto entityToResponseDto(ClienteEntity entity);
}
