package com.raissa.payments.service.administrativo.tarifario.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.administrativo.response.tarifario.TarifarioDashboardResponse;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioOrganizacionClienteRepository;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioPeriodoRepository;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioSuscripcionRepository;
import com.raissa.payments.service.administrativo.tarifario.TarifarioDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TarifarioDashboardServiceImpl implements TarifarioDashboardService {
    private final TarifarioOrganizacionClienteRepository organizacionClienteRepository;
    private final TarifarioSuscripcionRepository suscripcionRepository;
    private final TarifarioPeriodoRepository periodoRepository;

    @Value("${raissa.sistema.codigo}")
    private String codigoSistema;

    @Override
    public TarifarioDashboardResponse obtenerDashboard(
            Long clienteId) {

        var organizacionCliente = organizacionClienteRepository.findByClienteCodigoAndEstadoRegistro(clienteId, Constante.ESTADO_ACTIVO)
                .orElseThrow(() ->
                        new RuntimeException("Cliente sin organización asociada"));

        var suscripcion = suscripcionRepository.obtenerSuscripcionActiva(organizacionCliente
                        .getOrganizacion()
                        .getId(),
                        codigoSistema)
                .orElseThrow(() ->
                        new RuntimeException("No existe suscripción activa"));

        var periodo = periodoRepository.obtenerPeriodoVigenteBySuscripcion(suscripcion.getSuscripcionId())
                .orElseThrow(() ->
                        new RuntimeException("No existe período vigente"));

        Integer consumosDisponibles = Math.max(periodo.getLimiteConsumos() - periodo.getConsumosFacturables(), 0);

        Integer usuariosDisponibles = Math.max(periodo.getLimiteUsuarios() - periodo.getUsuariosUtilizados(), 0);

        BigDecimal porcentajeConsumo = BigDecimal.ZERO;

        if (periodo.getLimiteConsumos() > 0) {
            porcentajeConsumo = BigDecimal.valueOf(periodo.getConsumosFacturables())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(periodo.getLimiteConsumos()), 2, RoundingMode.HALF_UP);
        }

        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), suscripcion.getFechaFin());

        return TarifarioDashboardResponse.builder()
                .sistema(suscripcion.getPlan()
                        .getSistema()
                        .getNombre())
                .plan(suscripcion.getPlan()
                        .getNombre())
                .fechaInicio(suscripcion.getFechaInicio())
                .fechaFin(suscripcion.getFechaFin())
                .diasRestantes((int) Math.max(diasRestantes, 0))
                .usuariosContratados(periodo.getLimiteUsuarios())
                .usuariosUtilizados(periodo.getUsuariosUtilizados())
                .usuariosDisponibles(usuariosDisponibles)
                .consumosContratados(periodo.getLimiteConsumos())
                .consumosUtilizados(periodo.getConsumosFacturables())
                .consumosDisponibles(consumosDisponibles)
                .porcentajeConsumo(porcentajeConsumo)
                .periodoPrueba(suscripcion.getPeriodoPrueba())
                .suscripcionActiva(!Boolean.TRUE.equals(suscripcion.getCancelada()))
                .build();
    }
}
