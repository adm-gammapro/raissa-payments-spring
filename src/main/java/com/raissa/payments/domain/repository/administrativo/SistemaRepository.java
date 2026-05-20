package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SistemaRepository extends JpaRepository<SistemaEntity, String> {
    List<SistemaEntity> findByIdInAndEstadoRegistro(List<String> codigosSistemas, String estadoRegistro);
}
