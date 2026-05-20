package com.raissa.payments.service.administrativo.usuario.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.administrativo.request.usuariocliente.TransferirClientesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariocliente.ClienteAsignacionDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariocliente.UsuarioClientesTransferDto;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioClienteEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioSistemaEntity;
import com.raissa.payments.domain.repository.administrativo.ClienteRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioClienteRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.exception.commons.BusinessException;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.usuario.UsuarioClienteService;
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
public class UsuarioClienteServiceImpl extends AbstractRaissaPaymentsService implements UsuarioClienteService {
    private final UsuarioClienteRepository usuarioClienteRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    private static final String ESTADO_ACTIVO = "S";

    @Override
    @Transactional(readOnly = true)
    public UsuarioClientesTransferDto obtenerClientesParaTransferencia(Long usuarioId) {
        log.info("Obteniendo clientes para transferencia del usuario: {}", usuarioId);

        // Verificar que el usuario existe
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // Obtener SOLO los clientes que el current user tiene asignados y activos
        List<ClienteEntity> clientesDelCurrentUser = usuarioClienteRepository
                .findByUsuarioUsernameAndEstadoRegistro(getCurrentUser(), Constante.ESTADO_ACTIVO)
                .stream()
                .map(UsuarioClienteEntity::getCliente)
                .filter(cliente -> cliente != null && Constante.ESTADO_ACTIVO.equals(cliente.getEstadoRegistro()))
                .toList();

        if (clientesDelCurrentUser.isEmpty()) {
            log.warn("El usuario actual {} no tiene sistemas asignados", getCurrentUser());
            throw new BusinessException("No tienes sistemas asignados para transferir");
        }

        // Obtener clientes ya asignados al usuario
        Set<Long> clientesAsignadosIds = usuarioClienteRepository
                .findByUsuarioIdAndEstadoRegistro(usuarioId, ESTADO_ACTIVO)
                .stream()
                .map(uc -> uc.getCliente().getCodigo())
                .collect(Collectors.toSet());

        // Separar en disponibles y asignados
        List<ClienteAsignacionDto> clientesAsignados = new ArrayList<>();
        List<ClienteAsignacionDto> clientesDisponibles = new ArrayList<>();

        for (ClienteEntity cliente : clientesDelCurrentUser) {
            ClienteAsignacionDto dto = ClienteAsignacionDto.builder()
                    .id(cliente.getCodigo())
                    .razonSocial(cliente.getRazonSocial())
                    .ruc(cliente.getRuc())
                    .build();

            if (clientesAsignadosIds.contains(cliente.getCodigo())) {
                dto.setAsignado(true);
                clientesAsignados.add(dto);
            } else {
                dto.setAsignado(false);
                clientesDisponibles.add(dto);
            }
        }

        String nombreCompleto = String.format("%s %s %s",
                usuario.getNombres(),
                usuario.getApePaterno(),
                usuario.getApeMaterno());

        return UsuarioClientesTransferDto.builder()
                .usuarioId(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(nombreCompleto)
                .clientesDisponibles(clientesDisponibles)
                .clientesAsignados(clientesAsignados)
                .build();
    }

    @Override
    public void transferirClientes(TransferirClientesRequestDto transfer, HttpServletRequest request) {
        log.info("Transferencia de clientes - Usuario: {} - Asignar: {} - Desasignar: {}",
                transfer.getUsuarioId(), transfer.getClientesIdsAsignar(), transfer.getClientesIdsDesasignar());

        UsuarioEntity usuario = usuarioRepository.findById(transfer.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + transfer.getUsuarioId()));

        // 1. ASIGNAR nuevos clientes
        if (transfer.getClientesIdsAsignar() != null && !transfer.getClientesIdsAsignar().isEmpty()) {
            List<ClienteEntity> clientesAsignar = clienteRepository
                    .findByCodigoInAndEstadoRegistro(transfer.getClientesIdsAsignar(), ESTADO_ACTIVO);

            List<UsuarioClienteEntity> nuevasAsignaciones = new ArrayList<>();

            for (ClienteEntity cliente : clientesAsignar) {
                boolean yaAsignado = usuarioClienteRepository
                        .existsByUsuarioIdAndClienteCodigoAndEstadoRegistro(
                                transfer.getUsuarioId(), cliente.getCodigo(), ESTADO_ACTIVO);

                if (!yaAsignado) {
                    // Verificar si existe una asignación previa inactiva
                    Optional<UsuarioClienteEntity> asignacionExistente = usuarioClienteRepository
                            .findByUsuarioIdAndClienteCodigoAndEstadoRegistro(
                                    transfer.getUsuarioId(), cliente.getCodigo(), "N");

                    if (asignacionExistente.isPresent()) {
                        // Reactivar
                        UsuarioClienteEntity existente = asignacionExistente.get();
                        existente.setEstadoRegistro(ESTADO_ACTIVO);
                        setModAuditFields(existente,request);
                        nuevasAsignaciones.add(existente);
                    } else {
                        // Crear nueva
                        UsuarioClienteEntity nueva = UsuarioClienteEntity.builder()
                                .usuario(usuario)
                                .cliente(cliente)
                                .estadoRegistro(ESTADO_ACTIVO)
                                .build();
                        setInsAuditFields(nueva, request);
                        nuevasAsignaciones.add(nueva);
                    }
                }
            }

            if (!nuevasAsignaciones.isEmpty()) {
                usuarioClienteRepository.saveAll(nuevasAsignaciones);
                log.info("Asignados {} clientes al usuario: {}", nuevasAsignaciones.size(), transfer.getUsuarioId());
            }
        }

        // 2. DESASIGNAR clientes
        if (transfer.getClientesIdsDesasignar() != null && !transfer.getClientesIdsDesasignar().isEmpty()) {
            int desasignados = usuarioClienteRepository.desasignarClientes(
                    transfer.getUsuarioId(),
                    transfer.getClientesIdsDesasignar());
            log.info("Desasignados {} clientes del usuario: {}", desasignados, transfer.getUsuarioId());
        }
    }
}