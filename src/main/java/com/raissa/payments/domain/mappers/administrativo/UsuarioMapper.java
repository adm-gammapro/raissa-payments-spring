package com.raissa.payments.domain.mappers.administrativo;

import com.raissa.payments.domain.dto.administrativo.request.UsuarioRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.mappers.commons.MapperUtil;
import com.raissa.payments.domain.mappers.commons.TipoDocumentoMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper para mapear la entidad {@link UsuarioEntity} a sus correspondientes DTOs y viceversa
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
public abstract class UsuarioMapper extends EntityMapper<UsuarioEntity, Long> {
    protected UsuarioMapper() {
        super(UsuarioEntity.class);
    }

    /**
     * Carga los datos de un entity en un dto
     *
     * @param entity datos de la entidad
     * @return {@link UsuarioResponseDto}
     */
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "codigoTipoDocumento", source = "tipoDocumento.codigo")
    @Mapping(target = "descripcionTipoDocumento", source = "tipoDocumento.descripcion")
    @Mapping(target = "fechaCambioClave",     source = "fechaCambioClave",     qualifiedByName = "mapLocalDateToString")
    @Mapping(target = "fechaExpiracionClave", source = "fechaExpiracionClave", qualifiedByName = "mapLocalDateToString")
    public abstract UsuarioResponseDto entityToResponseDto(UsuarioEntity entity);

    /**
     * Carga los datos de un dto a un entity
     *
     * @param dto datos del dto
     * @return {@link UsuarioEntity}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoDocumento", source = "codigoTipoDocumento")
    @Mapping(target = "fechaCambioClave", source = "fechaCambioClave", qualifiedByName = "emptyToNull")
    @Mapping(target = "fechaExpiracionClave", source = "fechaExpiracionClave", qualifiedByName = "emptyToNull")
    @Mapping(target = "username", ignore = true)
    public abstract UsuarioEntity dtoToEntity(UsuarioRequestDto dto);

    /**
     * Actualiza un {@link UsuarioEntity} con los datos de un {@link UsuarioRequestDto}
     *
     * @param entity {@link UsuarioEntity} a actualizar
     * @param dto {@link UsuarioRequestDto} con los datos a actualizar
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoDocumento", source = "codigoTipoDocumento")
    @Mapping(target = "fechaCambioClave", source = "fechaCambioClave", qualifiedByName = "emptyToNull")
    @Mapping(target = "fechaExpiracionClave", source = "fechaExpiracionClave", qualifiedByName = "emptyToNull")
    @Mapping(target = "expired", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "credentialsExpired", ignore = true)
    @Mapping(target = "disabled", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "audiFechIns", ignore = true)
    @Mapping(target = "audiUsuario", ignore = true)
    @Mapping(target = "audiNomTerminal", ignore = true)
    @Mapping(target = "audiIp", ignore = true)
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract void update(@MappingTarget UsuarioEntity entity, UsuarioRequestDto dto);

    @AfterMapping
    protected void setUsername(UsuarioRequestDto dto, @MappingTarget UsuarioEntity entity) {
        if (dto == null) return;
        String numDoc = dto.getNumeroDocumento();
        if (entity.getTipoDocumento() != null && numDoc != null) {
            String abrev = entity.getTipoDocumento().getAbreviatura();
            entity.setUsername((abrev != null ? abrev : "") + "_" + numDoc);
        }
    }

    @AfterMapping
    protected void setUsernameOnBuild(UsuarioRequestDto dto,
                                      @MappingTarget UsuarioEntity.UsuarioEntityBuilder<?, ?> builder) {
        if (dto == null) return;
        if (builder.build().getTipoDocumento() != null && dto.getNumeroDocumento() != null) {
            builder.username((builder.build().getTipoDocumento().getAbreviatura() != null ? builder.build().getTipoDocumento().getAbreviatura() : "") + "_" + dto.getNumeroDocumento());
        }
    }
}
