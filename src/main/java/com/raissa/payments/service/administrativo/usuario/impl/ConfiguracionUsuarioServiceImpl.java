package com.raissa.payments.service.administrativo.usuario.impl;

import com.raissa.payments.domain.dto.administrativo.request.configuracionusuario.ConfiguracionUsuarioRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.ClienteOpcionDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.ConfiguracionUsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.OpcionesConfiguracionDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.PerfilOpcionDto;
import com.raissa.payments.domain.dto.administrativo.response.configuracionusuario.SistemaOpcionDto;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.ConfiguracionUsuarioEntity;
import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.repository.administrativo.ClienteRepository;
import com.raissa.payments.domain.repository.administrativo.ConfiguracionUsuarioRepository;
import com.raissa.payments.domain.repository.administrativo.PerfilRepository;
import com.raissa.payments.domain.repository.administrativo.SistemaRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioClienteRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioPerfilRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioSistemaRepository;
import com.raissa.payments.exception.commons.BusinessException;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.usuario.ConfiguracionUsuarioService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConfiguracionUsuarioServiceImpl extends AbstractRaissaPaymentsService implements ConfiguracionUsuarioService {
    private final ConfiguracionUsuarioRepository configuracionUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioClienteRepository usuarioClienteRepository;
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final UsuarioPerfilRepository usuarioPerfilRepository;
    private final ClienteRepository clienteRepository;
    private final SistemaRepository sistemaRepository;
    private final PerfilRepository perfilRepository;

    private static final String ESTADO_ACTIVO = "S";

    @Override
    @Transactional(readOnly = true)
    public OpcionesConfiguracionDto obtenerOpcionesConfiguracion(Long usuarioId) {
        log.info("Obteniendo opciones de configuración para usuario: {}", usuarioId);

        // Obtener clientes del usuario
        List<ClienteEntity> clientes = usuarioClienteRepository
                .findClientesByUsuarioId(usuarioId, ESTADO_ACTIVO);

        List<ClienteOpcionDto> clientesDto = clientes.stream()
                .map(c -> ClienteOpcionDto.builder()
                        .id(c.getCodigo())
                        .razonSocial(c.getRazonSocial())
                        .ruc(c.getRuc())
                        .build())
                .collect(Collectors.toList());

        // Obtener sistemas del usuario
        List<SistemaEntity> sistemas = usuarioSistemaRepository
                .findSistemasByUsuarioId(usuarioId, ESTADO_ACTIVO);

        List<SistemaOpcionDto> sistemasDto = sistemas.stream()
                .map(s -> SistemaOpcionDto.builder()
                        .id(s.getId())
                        .nombre(s.getNombre())
                        .build())
                .collect(Collectors.toList());

        // Obtener perfiles del usuario
        List<PerfilEntity> perfiles = usuarioPerfilRepository
                .findPerfilesByUsuarioId(usuarioId, ESTADO_ACTIVO);

        List<PerfilOpcionDto> perfilesDto = perfiles.stream()
                .map(p -> PerfilOpcionDto.builder()
                        .id(p.getCodigo())
                        .descripcion(p.getDescripcion())
                        .abreviatura(p.getAbreviatura())
                        .build())
                .collect(Collectors.toList());

        return OpcionesConfiguracionDto.builder()
                .clientes(clientesDto)
                .sistemas(sistemasDto)
                .perfiles(perfilesDto)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfiguracionUsuarioResponseDto> listarConfiguracionesPorUsuario(Long usuarioId) {
        log.info("Listando configuraciones del usuario: {}", usuarioId);

        List<ConfiguracionUsuarioEntity> configuraciones = configuracionUsuarioRepository
                .findConfiguracionesByUsuarioIdWithDetails(usuarioId, ESTADO_ACTIVO);

        return configuraciones.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConfiguracionUsuarioResponseDto guardarConfiguracion(ConfiguracionUsuarioRequestDto transfer, HttpServletRequest request) {
        log.info("Guardando configuración para usuario: {} - {}",
                transfer.getUsuarioId(), transfer.getIdConfiguracion() == null ? "NUEVA" : "EDICIÓN");

        // Validar que el usuario existe
        UsuarioEntity usuario = usuarioRepository.findById(transfer.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + transfer.getUsuarioId()));

        // Validar que el cliente existe y está asignado al usuario
        ClienteEntity cliente = clienteRepository.findById(transfer.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + transfer.getClienteId()));

        boolean clienteAsignado = usuarioClienteRepository
                .existsByUsuarioIdAndClienteCodigoAndEstadoRegistro(
                        transfer.getUsuarioId(), transfer.getClienteId(), ESTADO_ACTIVO);

        if (!clienteAsignado) {
            throw new BusinessException("El cliente no está asignado al usuario");
        }

        // Validar que el sistema existe y está asignado al usuario
        SistemaEntity sistema = sistemaRepository.findById(transfer.getSistemaId())
                .orElseThrow(() -> new NotFoundException("Sistema no encontrado con ID: " + transfer.getSistemaId()));

        boolean sistemaAsignado = usuarioSistemaRepository
                .existsByUsuarioIdAndSistemaIdAndEstadoRegistro(
                        transfer.getUsuarioId(), transfer.getSistemaId(), ESTADO_ACTIVO);

        if (!sistemaAsignado) {
            throw new BusinessException("El sistema no está asignado al usuario");
        }

        // Validar que el perfil existe y está asignado al usuario
        PerfilEntity perfil = perfilRepository.findById(transfer.getPerfilId())
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado con ID: " + transfer.getPerfilId()));

        boolean perfilAsignado = usuarioPerfilRepository
                .existsByUsuarioIdAndPerfilCodigoAndEstadoRegistro(
                        transfer.getUsuarioId(), transfer.getPerfilId(), ESTADO_ACTIVO);

        if (!perfilAsignado) {
            throw new BusinessException("El perfil no está asignado al usuario");
        }

        ConfiguracionUsuarioEntity configuracion;

        if (transfer.getIdConfiguracion() != null) {
            // Edición: obtener configuración existente
            configuracion = configuracionUsuarioRepository
                    .findByIdConfiguracionAndEstadoRegistro(transfer.getIdConfiguracion(), ESTADO_ACTIVO)
                    .orElseThrow(() -> new NotFoundException("Configuración no encontrada con ID: " + transfer.getIdConfiguracion()));

            // Validar que la configuración pertenece al usuario
            if (!configuracion.getUsuario().getId().equals(transfer.getUsuarioId())) {
                throw new BusinessException("La configuración no pertenece al usuario especificado");
            }
            setModAuditFields(configuracion, request);
        } else {
            // Creación: crear nueva entidad
            configuracion = ConfiguracionUsuarioEntity.builder()
                    .usuario(usuario)
                    .build();
            setInsAuditFields(configuracion, request);
        }

        // REGLA DE NEGOCIO: No se puede asignar más de un perfil al mismo cliente
        boolean existeConfiguracionMismoCliente = configuracionUsuarioRepository
                .existsByUsuarioIdAndClienteIdExcludingId(
                        transfer.getUsuarioId(), transfer.getClienteId(), ESTADO_ACTIVO, transfer.getIdConfiguracion());

        if (existeConfiguracionMismoCliente) {
            throw new BusinessException("No se puede asignar más de un perfil al mismo cliente. " +
                    "El cliente " + cliente.getRazonSocial() + " ya tiene una configuración asignada.");
        }

        // Verificar que no exista la misma configuración exacta
        boolean existeDuplicado = configuracionUsuarioRepository
                .existsDuplicateConfiguracion(
                        transfer.getUsuarioId(), transfer.getClienteId(),
                        transfer.getSistemaId(), transfer.getPerfilId(),
                        ESTADO_ACTIVO, transfer.getIdConfiguracion());

        if (existeDuplicado) {
            throw new BusinessException("La configuración ya existe para este usuario, cliente, sistema y perfil");
        }

        // Actualizar campos
        configuracion.setCliente(cliente);
        configuracion.setSistema(sistema);
        configuracion.setPerfil(perfil);

        if (transfer.getIdConfiguracion() == null) {
            configuracion.setEstadoRegistro(ESTADO_ACTIVO);
        }

        ConfiguracionUsuarioEntity saved = configuracionUsuarioRepository.save(configuracion);
        log.info("Configuración guardada exitosamente con ID: {}", saved.getIdConfiguracion());

        return mapToResponseDto(saved);
    }

    @Override
    public void eliminarConfiguracion(Long configuracionId, HttpServletRequest request) {
        log.info("Eliminando configuración con ID: {}", configuracionId);

        ConfiguracionUsuarioEntity configuracion = configuracionUsuarioRepository
                .findByIdConfiguracionAndEstadoRegistro(configuracionId, ESTADO_ACTIVO)
                .orElseThrow(() -> new NotFoundException("Configuración no encontrada con ID: " + configuracionId));

        configuracion.setEstadoRegistro("N");
        setModAuditFields(configuracion, request);

        configuracionUsuarioRepository.save(configuracion);
        log.info("Configuración eliminada exitosamente");
    }

    private ConfiguracionUsuarioResponseDto mapToResponseDto(ConfiguracionUsuarioEntity entity) {
        return ConfiguracionUsuarioResponseDto.builder()
                .idConfiguracion(entity.getIdConfiguracion())
                .usuarioId(entity.getUsuario().getId())
                .username(entity.getUsuario().getUsername())
                .usuarioNombre(String.format("%s %s %s",
                        entity.getUsuario().getNombres(),
                        entity.getUsuario().getApePaterno(),
                        entity.getUsuario().getApeMaterno()))
                .clienteId(entity.getCliente().getCodigo())
                .clienteRazonSocial(entity.getCliente().getRazonSocial())
                .clienteRuc(entity.getCliente().getRuc())
                .sistemaId(entity.getSistema().getId())
                .sistemaNombre(entity.getSistema().getNombre())
                .perfilId(entity.getPerfil().getCodigo())
                .perfilDescripcion(entity.getPerfil().getDescripcion())
                .perfilAbreviatura(entity.getPerfil().getAbreviatura())
                .estadoRegistro(entity.getEstadoRegistro())
                .build();
    }
}