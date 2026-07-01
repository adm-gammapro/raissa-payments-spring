package com.raissa.payments.domain.repository.administrativo.tarifario;

import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioOrganizacionClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarifarioOrganizacionClienteRepository extends JpaRepository<TarifarioOrganizacionClienteEntity, Long> {
    Optional<TarifarioOrganizacionClienteEntity> findByClienteCodigoAndEstadoRegistro(Long clienteId, String estadoRegistro);

    List<TarifarioOrganizacionClienteEntity> findByOrganizacionIdAndEstadoRegistro(Long organizacionId, String estadoRegistro);
}