package com.raissa.payments.domain.mappers.commons;

import com.raissa.payments.domain.dto.commons.EstadoSolicitudDto;
import com.raissa.payments.domain.entity.commons.EntityMapper;
import com.raissa.payments.domain.entity.commons.EstadoSolicitudEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                MapperUtil.class
        })
public abstract class EstadoSolicitudMapper extends EntityMapper<EstadoSolicitudEntity, String> {
        protected EstadoSolicitudMapper() { super(EstadoSolicitudEntity.class); }

        public abstract EstadoSolicitudDto entityToResponseDto(EstadoSolicitudEntity entity);
}
