package com.raissa.payments.service.administrativo.usuario.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.administrativo.request.usuariosistema.TransferirSistemasRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariosistema.SistemaAsignacionDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariosistema.UsuarioSistemasTransferDto;
import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioSistemaEntity;
import com.raissa.payments.domain.repository.administrativo.SistemaRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioSistemaRepository;
import com.raissa.payments.exception.commons.BusinessException;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.usuario.UsuarioSistemaService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UsuarioSistemaServiceImpl extends AbstractRaissaPaymentsService implements UsuarioSistemaService {
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final SistemaRepository sistemaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UsuarioSistemasTransferDto obtenerSistemasParaTransferencia(Long usuarioId) {
        log.info("Obteniendo sistemas para transferencia del usuario: {}", usuarioId);

        // Verificar que el usuario existe
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // Obtener SOLO los sistemas que el current user tiene asignados y activos
        List<SistemaEntity> sistemasDelCurrentUser = usuarioSistemaRepository
                .findByUsuarioUsernameAndEstadoRegistro(getCurrentUser(), Constante.ESTADO_ACTIVO)
                .stream()
                .map(UsuarioSistemaEntity::getSistema)
                .filter(sistema -> sistema != null && Constante.ESTADO_ACTIVO.equals(sistema.getEstadoRegistro()))
                .toList();

        if (sistemasDelCurrentUser.isEmpty()) {
            log.warn("El usuario actual {} no tiene sistemas asignados", getCurrentUser());
            throw new BusinessException("No tienes sistemas asignados para transferir");
        }

        // Obtener sistemas ya asignados al usuario
        Set<String> sistemasAsignadosIds = usuarioSistemaRepository
                .findByUsuarioIdAndEstadoRegistro(usuarioId, Constante.ESTADO_ACTIVO)
                .stream()
                .map(vinculo -> vinculo.getSistema().getId())
                .collect(Collectors.toSet());

        // Separar en disponibles y asignados
        List<SistemaAsignacionDto> sistemasAsignados = new ArrayList<>();
        List<SistemaAsignacionDto> sistemasDisponibles = new ArrayList<>();

        for (SistemaEntity sistema : sistemasDelCurrentUser) {
            SistemaAsignacionDto dto = SistemaAsignacionDto.builder()
                    .id(sistema.getId())
                    .nombre(sistema.getNombre())
                    .build();

            if (sistemasAsignadosIds.contains(sistema.getId())) {
                dto.setAsignado(true);
                sistemasAsignados.add(dto);
            } else {
                dto.setAsignado(false);
                sistemasDisponibles.add(dto);
            }
        }

        String nombreCompleto = String.format("%s %s %s",
                usuario.getNombres(),
                usuario.getApePaterno(),
                usuario.getApeMaterno());

        return UsuarioSistemasTransferDto.builder()
                .usuarioId(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(nombreCompleto)
                .sistemasDisponibles(sistemasDisponibles)
                .sistemasAsignados(sistemasAsignados)
                .build();
    }

    @Override
    public void transferirSistemas(TransferirSistemasRequestDto transfer, HttpServletRequest request) {
        log.info("Transferencia de sistemas - Usuario: {} - Asignar: {} - Desasignar: {}",
                transfer.getUsuarioId(), transfer.getSistemasIdsAsignar(), transfer.getSistemasIdsDesasignar());

        UsuarioEntity usuario = usuarioRepository.findById(transfer.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + transfer.getUsuarioId()));

        // 1. ASIGNAR nuevos sistemas (mover a la derecha)
        if (transfer.getSistemasIdsAsignar() != null && !transfer.getSistemasIdsAsignar().isEmpty()) {
            List<SistemaEntity> sistemasAsignar = sistemaRepository.findByIdInAndEstadoRegistro(transfer.getSistemasIdsAsignar(), Constante.ESTADO_ACTIVO);

            List<UsuarioSistemaEntity> nuevasAsignaciones = new ArrayList<>();

            for (SistemaEntity sistema : sistemasAsignar) {
                boolean yaAsignado = usuarioSistemaRepository
                        .existsByUsuarioIdAndSistemaIdAndEstadoRegistro(
                                transfer.getUsuarioId(), sistema.getId(), Constante.ESTADO_ACTIVO);

                if (!yaAsignado) {
                    // Verificar si existe una asignación previa inactiva
                    Optional<UsuarioSistemaEntity> asignacionExistente = usuarioSistemaRepository
                            .findByUsuarioIdAndSistemaIdAndEstadoRegistro(
                                    transfer.getUsuarioId(), sistema.getId(), "N");

                    if (asignacionExistente.isPresent()) {
                        // Reactivar
                        UsuarioSistemaEntity existente = asignacionExistente.get();
                        existente.setEstadoRegistro(Constante.ESTADO_ACTIVO);
                        setModAuditFields(existente, request);
                        nuevasAsignaciones.add(existente);
                    } else {
                        // Crear nueva
                        UsuarioSistemaEntity nueva = UsuarioSistemaEntity.builder()
                                .usuario(usuario)
                                .sistema(sistema)
                                .build();
                        setInsAuditFields(nueva, request);
                        nuevasAsignaciones.add(nueva);
                    }
                }
            }

            if (!nuevasAsignaciones.isEmpty()) {
                usuarioSistemaRepository.saveAll(nuevasAsignaciones);
                log.info("Asignados {} sistemas al usuario: {}", nuevasAsignaciones.size(), transfer.getUsuarioId());
            }
        }

        // 2. DESASIGNAR sistemas (mover a la izquierda) - Borrado lógico
        if (transfer.getSistemasIdsDesasignar() != null && !transfer.getSistemasIdsDesasignar().isEmpty()) {
            int desasignados = usuarioSistemaRepository.desasignarSistemas(
                    transfer.getUsuarioId(),
                    transfer.getSistemasIdsDesasignar());
            log.info("Desasignados {} sistemas del usuario: {}", desasignados, transfer.getUsuarioId());
        }
    }
}
