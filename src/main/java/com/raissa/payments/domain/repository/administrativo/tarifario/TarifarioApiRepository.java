package com.raissa.payments.domain.repository.administrativo.tarifario;

import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarifarioApiRepository extends JpaRepository<TarifarioApiEntity, Long> {
    Optional<TarifarioApiEntity> findBySistemaIdAndCodigoAndEstadoRegistro(String sistemaId,
                                                                           String codigo,
                                                                           String estadoRegistro);

    List<TarifarioApiEntity> findBySistemaIdAndEstadoRegistroOrderByNombre(String sistemaId,
                                                                           String estadoRegistro);

    Optional<TarifarioApiEntity> findBySistemaIdAndCodigo(String sistemaId,
                                                          String codigo);
}
