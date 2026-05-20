package com.raissa.payments.domain.repository.administrativo;

import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    List<ClienteEntity> findByEstadoRegistroOrderByRazonSocialAsc(String estadoRegistro);

    List<ClienteEntity> findByCodigoInAndEstadoRegistro(List<Long> ids, String estadoRegistro);
}
