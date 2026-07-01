package com.raissa.payments.domain.mappers.administrativo;

import com.raissa.payments.domain.dto.administrativo.request.PerfilRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.PerfilResponseDto;
import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.mappers.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para mapear la entidad {@link PerfilEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring",
        uses = {
                MapperUtil.class
        }
)
public abstract class PerfilMapper extends EntityMapper<PerfilEntity, Long> {
    protected PerfilMapper() {
        super(PerfilEntity.class);
    }

    /**
     * Carga los datos de un entity en un dto
     *
     * @param entity datos de la entidad
     * @return {@link PerfilResponseDto}
     */
    @Mapping(target = "fechaCaducidad", source = "fechaCaducidad", qualifiedByName = "mapLocalDateToStringInternacional")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    public abstract PerfilResponseDto entityToResponseDto(PerfilEntity entity);

    /**
     * Carga los datos de un dto a un entity
     *
     * @param dto datos del dto
     * @return {@link PerfilEntity}
     */
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "fechaCaducidad", source = "fechaCaducidad", qualifiedByName = "mapStringToLocalDateInternacional")
    public abstract PerfilEntity dtoToEntity(PerfilRequestDto dto);

    /**
     * Actualiza un {@link PerfilEntity} con los datos de un {@link PerfilRequestDto}
     *
     * @param entity {@link PerfilEntity} a actualizar
     * @param dto {@link PerfilRequestDto} con los datos a actualizar
     */
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "fechaCaducidad", source = "fechaCaducidad", qualifiedByName = "mapStringToLocalDateInternacional")
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "audiFechIns", ignore = true)
    @Mapping(target = "audiUsuario", ignore = true)
    @Mapping(target = "audiNomTerminal", ignore = true)
    @Mapping(target = "audiIp", ignore = true)
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract void update(@MappingTarget PerfilEntity entity, PerfilRequestDto dto);
}