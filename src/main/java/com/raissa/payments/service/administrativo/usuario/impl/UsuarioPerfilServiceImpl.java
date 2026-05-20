package com.raissa.payments.service.administrativo.usuario.impl;

import com.raissa.payments.domain.dto.administrativo.request.usuarioperfil.TransferirPerfilesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuarioperfil.PerfilAsignacionDto;
import com.raissa.payments.domain.dto.administrativo.response.usuarioperfil.UsuarioPerfilesTransferDto;
import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioPerfilEntity;
import com.raissa.payments.domain.repository.administrativo.PerfilRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioPerfilRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.usuario.UsuarioPerfilService;
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
public class UsuarioPerfilServiceImpl extends AbstractRaissaPaymentsService implements UsuarioPerfilService {
    private final UsuarioPerfilRepository usuarioPerfilRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    private static final String ESTADO_ACTIVO = "S";

    @Override
    @Transactional(readOnly = true)
    public UsuarioPerfilesTransferDto obtenerPerfilesParaTransferencia(Long usuarioId) {
        log.info("Obteniendo perfiles para transferencia del usuario: {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + usuarioId));

        List<PerfilEntity> todosLosPerfiles = perfilRepository.findPerfilesByUsernameOrderByDescripcionAsc(getCurrentUser());

        Set<Long> perfilesAsignadosIds = usuarioPerfilRepository
                .findByUsuarioIdAndEstadoRegistro(usuarioId, ESTADO_ACTIVO)
                .stream()
                .map(up -> up.getPerfil().getCodigo())
                .collect(Collectors.toSet());

        List<PerfilAsignacionDto> perfilesAsignados = new ArrayList<>();
        List<PerfilAsignacionDto> perfilesDisponibles = new ArrayList<>();

        for (PerfilEntity perfil : todosLosPerfiles) {
            PerfilAsignacionDto dto = PerfilAsignacionDto.builder()
                    .id(perfil.getCodigo())
                    .descripcion(perfil.getDescripcion())
                    .abreviatura(perfil.getAbreviatura())
                    .nombreComercial(perfil.getNombreComercial())
                    .build();

            if (perfilesAsignadosIds.contains(perfil.getCodigo())) {
                dto.setAsignado(true);
                perfilesAsignados.add(dto);
            } else {
                dto.setAsignado(false);
                perfilesDisponibles.add(dto);
            }
        }

        String nombreCompleto = String.format("%s %s %s",
                usuario.getNombres(),
                usuario.getApePaterno(),
                usuario.getApeMaterno());

        return UsuarioPerfilesTransferDto.builder()
                .usuarioId(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(nombreCompleto)
                .perfilesDisponibles(perfilesDisponibles)
                .perfilesAsignados(perfilesAsignados)
                .build();
    }

    @Override
    public void transferirPerfiles(TransferirPerfilesRequestDto transfer, HttpServletRequest request) {
        log.info("Transferencia de perfiles - Usuario: {} - Asignar: {} - Desasignar: {}",
                transfer.getUsuarioId(), transfer.getPerfilesIdsAsignar(), transfer.getPerfilesIdsDesasignar());

        UsuarioEntity usuario = usuarioRepository.findById(transfer.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + transfer.getUsuarioId()));

        // 1. ASIGNAR nuevos perfiles
        if (transfer.getPerfilesIdsAsignar() != null && !transfer.getPerfilesIdsAsignar().isEmpty()) {
            List<PerfilEntity> perfilesAsignar = perfilRepository.findByCodigoInAndEstadoRegistro(transfer.getPerfilesIdsAsignar(), ESTADO_ACTIVO);

            List<UsuarioPerfilEntity> nuevasAsignaciones = new ArrayList<>();

            for (PerfilEntity perfil : perfilesAsignar) {
                boolean yaAsignado = usuarioPerfilRepository
                        .existsByUsuarioIdAndPerfilCodigoAndEstadoRegistro(
                                transfer.getUsuarioId(), perfil.getCodigo(), ESTADO_ACTIVO);

                if (!yaAsignado) {
                    Optional<UsuarioPerfilEntity> asignacionExistente = usuarioPerfilRepository
                            .findByUsuarioIdAndPerfilCodigoAndEstadoRegistro(
                                    transfer.getUsuarioId(), perfil.getCodigo(), "N");

                    if (asignacionExistente.isPresent()) {
                        UsuarioPerfilEntity existente = asignacionExistente.get();
                        existente.setEstadoRegistro(ESTADO_ACTIVO);
                        setModAuditFields(existente, request);
                        nuevasAsignaciones.add(existente);
                    } else {
                        UsuarioPerfilEntity nueva = UsuarioPerfilEntity.builder()
                                .usuario(usuario)
                                .perfil(perfil)
                                .estadoRegistro(ESTADO_ACTIVO)
                                .build();
                        setInsAuditFields(nueva, request);
                        nuevasAsignaciones.add(nueva);
                    }
                }
            }

            if (!nuevasAsignaciones.isEmpty()) {
                usuarioPerfilRepository.saveAll(nuevasAsignaciones);
                log.info("Asignados {} perfiles al usuario: {}", nuevasAsignaciones.size(), transfer.getUsuarioId());
            }
        }

        // 2. DESASIGNAR perfiles
        if (transfer.getPerfilesIdsDesasignar() != null && !transfer.getPerfilesIdsDesasignar().isEmpty()) {
            int desasignados = usuarioPerfilRepository.desasignarPerfiles(
                    transfer.getUsuarioId(),
                    transfer.getPerfilesIdsDesasignar());
            log.info("Desasignados {} perfiles del usuario: {}", desasignados, transfer.getUsuarioId());
        }
    }
}