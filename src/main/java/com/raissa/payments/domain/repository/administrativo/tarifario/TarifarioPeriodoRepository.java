package com.raissa.payments.domain.repository.administrativo.tarifario;

import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioPeriodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TarifarioPeriodoRepository extends JpaRepository<TarifarioPeriodoEntity, Long> {
    @Query("""
                select p
                from TarifarioPeriodoEntity p
                where p.suscripcion.suscripcionId = :suscripcionId
                  and p.cerrado = false
                  and p.estadoRegistro = 'S'
            """)
    Optional<TarifarioPeriodoEntity> obtenerPeriodoActivo(@Param("suscripcionId") Long suscripcionId);

    @Query("""
            select p
            from TarifarioPeriodoEntity p
            where p.suscripcion.suscripcionId = :suscripcionId
            and current_date between p.fechaInicio and p.fechaFin
            """)
    Optional<TarifarioPeriodoEntity> obtenerPeriodoVigenteBySuscripcion(Long suscripcionId);

    @Query("""
        select p
        from TarifarioPeriodoEntity p
        where p.suscripcion.organizacion.id = :organizacionId
          and current_date between p.fechaInicio and p.fechaFin
          and p.cerrado = false
    """)
    Optional<TarifarioPeriodoEntity> obtenerPeriodoVigenteByOrganizacion(Long organizacionId);

    List<TarifarioPeriodoEntity> findBySuscripcionSuscripcionIdOrderByFechaInicioDesc(Long suscripcionId);


}