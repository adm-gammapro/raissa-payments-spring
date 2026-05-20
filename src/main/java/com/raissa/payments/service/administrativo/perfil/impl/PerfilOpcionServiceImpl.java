package com.raissa.payments.service.administrativo.perfil.impl;

import com.raissa.payments.domain.dto.administrativo.request.perfilopcion.TransferirOpcionesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilopcion.OpcionAsignacionDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilopcion.PerfilOpcionesTransferDto;
import com.raissa.payments.domain.entity.administrativo.OpcionEntity;
import com.raissa.payments.domain.entity.administrativo.OpcionPerfilEntity;
import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import com.raissa.payments.domain.repository.administrativo.OpcionPerfilRepository;
import com.raissa.payments.domain.repository.administrativo.OpcionRepository;
import com.raissa.payments.domain.repository.administrativo.PerfilRepository;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.perfil.PerfilOpcionService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PerfilOpcionServiceImpl extends AbstractRaissaPaymentsService implements PerfilOpcionService {
    private final OpcionPerfilRepository opcionPerfilRepository;
    private final OpcionRepository opcionRepository;
    private final PerfilRepository perfilRepository;

    @Value("${raissa.sistema.codigo}")
    private String sistemaId;

    private static final String ESTADO_ACTIVO = "S";

    @Override
    @Transactional(readOnly = true)
    public PerfilOpcionesTransferDto obtenerOpcionesParaTransferencia(Long perfilId) {
        log.info("Obteniendo opciones para transferencia del perfil: {}", perfilId);

        // Validar que el perfil existe
        PerfilEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con ID: " + perfilId));

        // Obtener opciones del sistema actual que son seleccionables
        List<OpcionEntity> todasLasOpciones = opcionRepository
                .findOpcionesBySistemaAndSeleccionable(sistemaId, ESTADO_ACTIVO);

        if (todasLasOpciones.isEmpty()) {
            log.warn("No se encontraron opciones seleccionables para el sistema: {}", sistemaId);
        }

        // Obtener opciones ya asignadas al perfil
        Set<Long> opcionesAsignadasIds = opcionPerfilRepository
                .findByPerfilCodigoAndEstadoRegistro(perfilId, ESTADO_ACTIVO)
                .stream()
                .map(op -> op.getOpcion().getCodigo())
                .collect(Collectors.toSet());

        List<OpcionAsignacionDto> opcionesAsignadas = new ArrayList<>();
        List<OpcionAsignacionDto> opcionesDisponibles = new ArrayList<>();

        for (OpcionEntity opcion : todasLasOpciones) {
            OpcionAsignacionDto dto = OpcionAsignacionDto.builder()
                    .id(opcion.getCodigo())
                    .descripcion(opcion.getDescripcionOpcion())
                    .ruta(opcion.getRutaOpcion())
                    .icono(opcion.getIcono())
                    .opcionPadre(opcion.getOpcionPadre())
                    .numeroOrden(opcion.getNumeroOrden())
                    .build();

            if (opcionesAsignadasIds.contains(opcion.getCodigo())) {
                dto.setAsignado(true);
                opcionesAsignadas.add(dto);
            } else {
                dto.setAsignado(false);
                opcionesDisponibles.add(dto);
            }
        }

        log.info("Perfil {} - Opciones disponibles: {}, asignadas: {}",
                perfilId, opcionesDisponibles.size(), opcionesAsignadas.size());

        return PerfilOpcionesTransferDto.builder()
                .perfilId(perfil.getCodigo())
                .descripcion(perfil.getDescripcion())
                .abreviatura(perfil.getAbreviatura())
                .nombreComercial(perfil.getNombreComercial())
                .opcionesDisponibles(opcionesDisponibles)
                .opcionesAsignadas(opcionesAsignadas)
                .build();
    }

    @Override
    public void transferirOpciones(TransferirOpcionesRequestDto transfer, HttpServletRequest request) {
        log.info("Transferencia de opciones - Perfil: {} - Asignar: {} - Desasignar: {}",
                transfer.getPerfilId(), transfer.getOpcionesIdsAsignar(), transfer.getOpcionesIdsDesasignar());

        PerfilEntity perfil = perfilRepository.findById(transfer.getPerfilId())
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con ID: " + transfer.getPerfilId()));

        // 1. ASIGNAR nuevas opciones
        if (transfer.getOpcionesIdsAsignar() != null && !transfer.getOpcionesIdsAsignar().isEmpty()) {
            List<OpcionEntity> opcionesAsignar = opcionRepository.findByCodigoInAndEstadoRegistro(transfer.getOpcionesIdsAsignar(), ESTADO_ACTIVO);

            List<OpcionPerfilEntity> nuevasAsignaciones = new ArrayList<>();

            for (OpcionEntity opcion : opcionesAsignar) {
                boolean yaAsignado = opcionPerfilRepository
                        .existsByPerfilCodigoAndOpcionCodigoAndEstadoRegistro(
                                transfer.getPerfilId(), opcion.getCodigo(), ESTADO_ACTIVO);

                if (!yaAsignado) {
                    Optional<OpcionPerfilEntity> asignacionExistente = opcionPerfilRepository
                            .findByPerfilCodigoAndOpcionCodigoAndEstadoRegistro(
                                    transfer.getPerfilId(), opcion.getCodigo(), "N");

                    if (asignacionExistente.isPresent()) {
                        OpcionPerfilEntity existente = asignacionExistente.get();
                        existente.setEstadoRegistro(ESTADO_ACTIVO);
                        setModAuditFields(existente, request);
                        nuevasAsignaciones.add(existente);
                    } else {
                        OpcionPerfilEntity nueva = OpcionPerfilEntity.builder()
                                .perfil(perfil)
                                .opcion(opcion)
                                .estadoRegistro(ESTADO_ACTIVO)
                                .build();
                        setInsAuditFields(nueva, request);
                        nuevasAsignaciones.add(nueva);
                    }
                }
            }

            if (!nuevasAsignaciones.isEmpty()) {
                opcionPerfilRepository.saveAll(nuevasAsignaciones);
                log.info("Asignadas {} opciones al perfil: {}", nuevasAsignaciones.size(), transfer.getPerfilId());
            }
        }

        // 2. DESASIGNAR opciones
        if (transfer.getOpcionesIdsDesasignar() != null && !transfer.getOpcionesIdsDesasignar().isEmpty()) {
            int desasignados = opcionPerfilRepository.desasignarOpciones(
                    transfer.getPerfilId(),
                    transfer.getOpcionesIdsDesasignar());
            log.info("Desasignadas {} opciones del perfil: {}", desasignados, transfer.getPerfilId());
        }
    }
}