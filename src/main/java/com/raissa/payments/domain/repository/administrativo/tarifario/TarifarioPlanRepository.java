package com.raissa.payments.domain.repository.administrativo.tarifario;

import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarifarioPlanRepository extends JpaRepository<TarifarioPlanEntity, Long> {
    Optional<TarifarioPlanEntity> findBySistemaIdAndCodigoAndEstadoRegistro(String sistemaId,
                                                                            String codigo,
                                                                            String estadoRegistro);

    List<TarifarioPlanEntity> findBySistemaIdAndEstadoRegistroOrderByNombre(String sistemaId,
                                                                            String estadoRegistro);
}
