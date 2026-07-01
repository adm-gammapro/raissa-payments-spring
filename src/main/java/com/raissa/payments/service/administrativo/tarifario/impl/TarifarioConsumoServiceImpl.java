package com.raissa.payments.service.administrativo.tarifario.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.administrativo.request.tarifario.RegistrarConsumoRequest;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioApiEntity;
import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioConsumoEntity;
import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioOrganizacionEntity;
import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioPeriodoEntity;
import com.raissa.payments.domain.entity.administrativo.tarifario.TarifarioSuscripcionEntity;
import com.raissa.payments.domain.repository.administrativo.ClienteRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioApiRepository;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioConsumoRepository;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioOrganizacionRepository;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioPeriodoRepository;
import com.raissa.payments.domain.repository.administrativo.tarifario.TarifarioSuscripcionRepository;
import com.raissa.payments.exception.commons.EjecucionException;
import com.raissa.payments.service.administrativo.tarifario.TarifarioConsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TarifarioConsumoServiceImpl implements TarifarioConsumoService {
    private final TarifarioApiRepository apiRepository;
    private final TarifarioOrganizacionRepository organizacionRepository;
    private final TarifarioPeriodoRepository periodoRepository;
    private final TarifarioConsumoRepository consumoRepository;
    private final TarifarioSuscripcionRepository suscripcionRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registrarConsumo(String keyId,
                                 String token,
                                 RegistrarConsumoRequest request) {

        TarifarioOrganizacionEntity organizacion = organizacionRepository.findByCodigoAndEstadoRegistro(keyId, Constante.ESTADO_ACTIVO).orElseThrow(
                () -> new RuntimeException("Organización no encontrada"));

        if (!passwordEncoder.matches(token, organizacion.getInternalTokenHash())) {
            throw new EjecucionException("Token inválido");
        }

        ClienteEntity cliente = clienteRepository.findById(request.getClienteId()).orElseThrow();

        UsuarioEntity usuario = usuarioRepository.findByUsernameAndEstadoRegistro(request.getUsername(), Constante.ESTADO_ACTIVO).orElseThrow();

        TarifarioSuscripcionEntity suscripcion = suscripcionRepository.obtenerSuscripcionActiva(organizacion.getId(), request.getSistemaId())
                .orElseThrow(() -> new RuntimeException("La organización no tiene una suscripción activa"));

        TarifarioPeriodoEntity periodo = periodoRepository.obtenerPeriodoActivo(suscripcion.getSuscripcionId()).orElseThrow();

        TarifarioApiEntity api = apiRepository.findBySistemaIdAndCodigo(request.getSistemaId(), request.getCodigoApi()).orElseThrow();

        boolean facturable = Boolean.TRUE.equals(request.getExitoso()) && Boolean.TRUE.equals(api.getFacturable());

        BigDecimal importe = facturable ? Optional.ofNullable(api.getPrecioUnitario()).orElse(BigDecimal.ZERO) : BigDecimal.ZERO;

        TarifarioConsumoEntity consumo = TarifarioConsumoEntity.builder()
                .periodo(periodo)
                .cliente(cliente)
                .usuario(usuario)
                .api(api)
                .fechaConsumo(LocalDateTime.now())
                .codigoResultado(request.getCodigoResultado())
                .exitoso(request.getExitoso())
                .facturable(facturable)
                .importe(importe)
                .observacion(request.getObservacion())
                .estadoRegistro(Constante.ESTADO_ACTIVO)
                .audiUsuario(request.getUsername())
                .audiFechIns(LocalDateTime.now())
                .audiIp("CONECTOR")
                .audiNomTerminal("CONECTOR")
                .build();

        consumoRepository.save(consumo);

        actualizarAcumuladosPeriodo(
                periodo,
                facturable,
                importe);
    }

    private void actualizarAcumuladosPeriodo(TarifarioPeriodoEntity periodo,
                                             boolean facturable,
                                             BigDecimal importe) {
        if (facturable) {
            periodo.setConsumosFacturables(periodo.getConsumosFacturables() + 1);
            periodo.setMontoConsumos(periodo.getMontoConsumos().add(importe));
        } else {
            periodo.setConsumosNoFacturables(periodo.getConsumosNoFacturables() + 1);
        }

        periodoRepository.save(periodo);
    }
}
