package com.raissa.payments.domain.repository.administrativo.tarifario;

import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioSuscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TarifarioSuscripcionRepository extends JpaRepository<TarifarioSuscripcionEntity, Long> {
    @Query("""
                select s
                from TarifarioSuscripcionEntity s
                where s.organizacion.id = :organizacionId
                  and s.plan.sistema.id = :sistemaId
                  and s.cancelada = false
                  and s.estadoRegistro = 'S'
                  and current_date between s.fechaInicio and s.fechaFin
            """)
    Optional<TarifarioSuscripcionEntity> obtenerSuscripcionActiva(@Param("organizacionId") Long organizacionId,
                                                                  @Param("sistemaId") String sistemaId);

    List<TarifarioSuscripcionEntity> findByOrganizacionIdAndEstadoRegistroOrderByFechaInicioDesc(Long organizacionId, String estadoRegistro);
}