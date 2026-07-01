package com.raissa.payments.domain.repository.administrativo.tarifario;

import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioConsumoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TarifarioConsumoRepository extends JpaRepository<TarifarioConsumoEntity, Long> {
    Page<TarifarioConsumoEntity> findByPeriodoPeriodoIdOrderByFechaConsumoDesc(Long periodoId,
                                                                               Pageable pageable);

    @Query("""
            select count(c)
            from TarifarioConsumoEntity c
            where c.periodo.periodoId = :periodoId
            and c.facturable = true
            """)
    Long contarFacturables(Long periodoId);

    @Query("""
            select count(c)
            from TarifarioConsumoEntity c
            where c.periodo.periodoId = :periodoId
            and c.facturable = false
            """)
    Long contarNoFacturables(Long periodoId);

    @Query("""
            select count(c)
            from TarifarioConsumoEntity c
            where c.periodo.periodoId = :periodoId
            and c.cliente.codigo = :clienteId
            and c.facturable = true
            """)
    Long contarPorCliente(Long periodoId,
                          Long clienteId);

    @Query("""
            select count(c)
            from TarifarioConsumoEntity c
            where c.periodo.periodoId = :periodoId
            and c.facturable = true
            """)
    Integer obtenerConsumoPeriodo(Long periodoId);
}