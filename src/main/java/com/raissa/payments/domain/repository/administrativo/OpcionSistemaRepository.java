package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.OpcionSistemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpcionSistemaRepository extends JpaRepository<OpcionSistemaEntity, Long> {
    List<OpcionSistemaEntity> findBySistemaIdAndEstadoRegistro(String sistemaId, String estadoRegistro);

    boolean existsBySistemaIdAndOpcionMenuAndEstadoRegistro(String sistemaId, Long opcionMenu, String estadoRegistro);
}