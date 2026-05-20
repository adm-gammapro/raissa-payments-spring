package com.raissa.payments.service.administrativo.perfil.impl;

import com.raissa.payments.domain.dto.administrativo.request.perfilcliente.TransferirClientesPerfilRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilcliente.ClienteAsignacionDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilcliente.PerfilClientesTransferDto;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.PerfilClienteEntity;
import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.repository.administrativo.ClienteRepository;
import com.raissa.payments.domain.repository.administrativo.PerfilClienteRepository;
import com.raissa.payments.domain.repository.administrativo.PerfilRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioClienteRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.perfil.PerfilClienteService;
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
public class PerfilClienteServiceImpl extends AbstractRaissaPaymentsService implements PerfilClienteService {
    private final PerfilClienteRepository perfilClienteRepository;
    private final ClienteRepository clienteRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioClienteRepository usuarioClienteRepository;

    private static final String ESTADO_ACTIVO = "S";

    @Override
    @Transactional(readOnly = true)
    public PerfilClientesTransferDto obtenerClientesParaTransferencia(Long perfilId) {
        log.info("Obteniendo clientes para transferencia del perfil: {}", perfilId);

        PerfilEntity perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con ID: " + perfilId));

        String currentUser = getCurrentUser();
        UsuarioEntity usuario = usuarioRepository.findByUsernameAndEstadoRegistro(currentUser, ESTADO_ACTIVO)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + currentUser));

        List<ClienteEntity> clientesDelUsuario = usuarioClienteRepository.findClientesByUsuarioIdAndEstadoRegistro(usuario.getId(), ESTADO_ACTIVO);

        Set<Long> clientesAsignadosIds = perfilClienteRepository
                .findByPerfilCodigoAndEstadoRegistro(perfilId, ESTADO_ACTIVO)
                .stream()
                .map(pc -> pc.getCliente().getCodigo())
                .collect(Collectors.toSet());

        List<ClienteAsignacionDto> clientesAsignados = new ArrayList<>();
        List<ClienteAsignacionDto> clientesDisponibles = new ArrayList<>();

        for (ClienteEntity cliente : clientesDelUsuario) {
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

        return PerfilClientesTransferDto.builder()
                .perfilId(perfil.getCodigo())
                .descripcion(perfil.getDescripcion())
                .abreviatura(perfil.getAbreviatura())
                .nombreComercial(perfil.getNombreComercial())
                .clientesDisponibles(clientesDisponibles)
                .clientesAsignados(clientesAsignados)
                .build();
    }

    @Override
    public void transferirClientes(TransferirClientesPerfilRequestDto transfer, HttpServletRequest request) {
        log.info("Transferencia de clientes a perfil - Perfil: {} - Asignar: {} - Desasignar: {}",
                transfer.getPerfilId(), transfer.getClientesIdsAsignar(), transfer.getClientesIdsDesasignar());

        PerfilEntity perfil = perfilRepository.findById(transfer.getPerfilId())
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con ID: " + transfer.getPerfilId()));

        // 1. ASIGNAR nuevos clientes
        if (transfer.getClientesIdsAsignar() != null && !transfer.getClientesIdsAsignar().isEmpty()) {
            List<ClienteEntity> clientesAsignar = clienteRepository.findByCodigoInAndEstadoRegistro(transfer.getClientesIdsAsignar(), ESTADO_ACTIVO);

            List<PerfilClienteEntity> nuevasAsignaciones = new ArrayList<>();

            for (ClienteEntity cliente : clientesAsignar) {
                boolean yaAsignado = perfilClienteRepository
                        .existsByPerfilCodigoAndClienteCodigoAndEstadoRegistro(
                                transfer.getPerfilId(), cliente.getCodigo(), ESTADO_ACTIVO);

                if (!yaAsignado) {
                    Optional<PerfilClienteEntity> asignacionExistente = perfilClienteRepository
                            .findByPerfilCodigoAndClienteCodigoAndEstadoRegistro(
                                    transfer.getPerfilId(), cliente.getCodigo(), "N");

                    if (asignacionExistente.isPresent()) {
                        PerfilClienteEntity existente = asignacionExistente.get();
                        existente.setEstadoRegistro(ESTADO_ACTIVO);
                        setModAuditFields(existente, request);
                        nuevasAsignaciones.add(existente);
                    } else {
                        PerfilClienteEntity nueva = PerfilClienteEntity.builder()
                                .perfil(perfil)
                                .cliente(cliente)
                                .estadoRegistro(ESTADO_ACTIVO)
                                .build();
                        setInsAuditFields(nueva, request);
                        nuevasAsignaciones.add(nueva);
                    }
                }
            }

            if (!nuevasAsignaciones.isEmpty()) {
                perfilClienteRepository.saveAll(nuevasAsignaciones);
                log.info("Asignados {} clientes al perfil: {}", nuevasAsignaciones.size(), transfer.getPerfilId());
            }
        }

        // 2. DESASIGNAR clientes
        if (transfer.getClientesIdsDesasignar() != null && !transfer.getClientesIdsDesasignar().isEmpty()) {
            int desasignados = perfilClienteRepository.desasignarClientes(
                    transfer.getPerfilId(),
                    transfer.getClientesIdsDesasignar());
            log.info("Desasignados {} clientes del perfil: {}", desasignados, transfer.getPerfilId());
        }
    }
}