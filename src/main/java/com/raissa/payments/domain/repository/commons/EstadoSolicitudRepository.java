package com.raissa.payments.domain.repository.commons;

import com.raissa.payments.domain.entity.commons.EstadoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoSolicitudRepository extends JpaRepository<EstadoSolicitudEntity, String> {
    List<EstadoSolicitudEntity> findByEstadoRegistroOrderByDescripcionAsc(String estadoRegistro);
}
