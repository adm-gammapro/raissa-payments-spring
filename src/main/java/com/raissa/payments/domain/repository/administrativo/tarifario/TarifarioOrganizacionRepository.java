package com.raissa.payments.domain.repository.administrativo.tarifario;

import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioOrganizacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarifarioOrganizacionRepository extends JpaRepository<TarifarioOrganizacionEntity, Long> {
    Optional<TarifarioOrganizacionEntity> findByCodigoAndEstadoRegistro(String codigo, String estadoRegistro);
}