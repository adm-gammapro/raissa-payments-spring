package com.raissa.payments.domain.repository.commons;

import com.raissa.payments.domain.entity.commons.TipoDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, String> {
    List<TipoDocumentoEntity> findByEstadoRegistro(String estadoRegistro);
}
