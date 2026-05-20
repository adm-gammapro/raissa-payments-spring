package com.raissa.payments.service.administrativo.usuario.impl;

import com.raissa.comun.util.Constante;
import com.raissa.comun.util.ConstanteError;
import com.raissa.payments.domain.dto.administrativo.request.UsuarioRequestDto;
import com.raissa.payments.domain.dto.administrativo.request.UsuarioResetPasswordRequetDto;
import com.raissa.payments.domain.dto.administrativo.request.UsuarioSearchDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioSearchResponseDto;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioClienteEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioSistemaEntity;
import com.raissa.payments.domain.entity.commons.TipoDocumentoEntity;
import com.raissa.payments.domain.mappers.administrativo.UsuarioMapper;
import com.raissa.payments.domain.repository.administrativo.ClienteRepository;
import com.raissa.payments.domain.repository.administrativo.SistemaRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioClienteRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.domain.repository.administrativo.UsuarioSistemaRepository;
import com.raissa.payments.domain.repository.commons.TipoDocumentoRepository;
import com.raissa.payments.exception.commons.BusinessException;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.usuario.UsuarioService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import com.raissa.payments.util.EmailService;
import com.raissa.payments.util.PasswordGeneratorService;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl extends AbstractRaissaPaymentsService implements UsuarioService {
    //Repositories
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioClienteRepository usuarioClienteRepository;
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final SistemaRepository sistemaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    //Utils
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordGeneratorService passwordGeneratorService;

    //Mappers
    private final UsuarioMapper usuarioMapper;

    @Value("${raissa.sistema.codigo}")
    private String codigoSistema;

    @Transactional(readOnly = true)
    public UsuarioResponseDto getUsuarioByUsername(String username) {
        UsuarioEntity entity = usuarioRepository.findByUsernameAndEstadoRegistro(username, Constante.ESTADO_ACTIVO)
                .orElseThrow(() -> new NotFoundException(ConstanteError.MENSAJE_ERROR_REGISTRO_NO_ENCONTRADO));

        return usuarioMapper.entityToResponseDto(entity);

    }

    public UsuarioSearchResponseDto getAllUsuario(UsuarioSearchDto usuarioSearch) {
        Pageable pageable = buildPageable(usuarioSearch);

        Optional<UsuarioEntity> usuario =usuarioRepository.findByUsernameAndEstadoRegistro(getCurrentUser(), Constante.ESTADO_ACTIVO);

        Page<UsuarioEntity> pageUsuarioEntity;
        if (usuario.isPresent()) {
            pageUsuarioEntity = usuarioRepository.searchByEmpresa(usuarioSearch.getEstadoRegistro(),
                    usuarioSearch.getNombreUsuario(),
                    usuario.get().getId(),
                    pageable);
        } else {
            throw new NotFoundException(ConstanteError.MENSAJE_ERROR_REGISTRO_NO_ENCONTRADO);
        }

        List<UsuarioResponseDto> list = pageUsuarioEntity.getContent()
                .stream()
                .map(usuarioMapper::entityToResponseDto)
                .toList();

        return new UsuarioSearchResponseDto(
                pageUsuarioEntity.getTotalPages(),
                pageUsuarioEntity.getTotalElements(),
                pageUsuarioEntity.getNumber(),
                pageUsuarioEntity.getSize(),
                list
        );
    }

    @Transactional
    public UsuarioResponseDto registrar(UsuarioRequestDto requestDto, HttpServletRequest request) {
        try {
            String claveTemporal = passwordGeneratorService.generarPasswordBancario();
            requestDto.setPassword(passwordEncoder.encode(claveTemporal));

            UsuarioEntity usuarioEntity = usuarioMapper.dtoToEntity(requestDto);
            usuarioEntity.setIndicadorExpiracion(Constante.INDICADOR_NO_EXPIRA_CLAVE);
            usuarioEntity.setTipoUsuario(Constante.TIPO_USUARIO_USER);
            usuarioEntity.setExpired(true);
            usuarioEntity.setLocked(true);
            usuarioEntity.setCredentialsExpired(true);
            usuarioEntity.setDisabled(true);
            setInsAuditFields(usuarioEntity, request);

            usuarioRepository.save(usuarioEntity);

            UsuarioClienteEntity usuarioCliente = new UsuarioClienteEntity();
            usuarioCliente.setUsuario(usuarioEntity);
            ClienteEntity cliente = clienteRepository.findById(requestDto.getIdEmpresa())
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado o desactivado"));
            usuarioCliente.setCliente(cliente);
            setInsAuditFields(usuarioCliente, request);
            usuarioClienteRepository.save(usuarioCliente);

            UsuarioSistemaEntity usuariosistema = new UsuarioSistemaEntity();
            usuariosistema.setUsuario(usuarioEntity);
            SistemaEntity sistema = sistemaRepository.findById(codigoSistema)
                    .orElseThrow(() -> new BusinessException("Sistema no encontrado con ID: " + codigoSistema));
            usuariosistema.setSistema(sistema);
            setInsAuditFields(usuariosistema, request);
            usuarioSistemaRepository.save(usuariosistema);

            TipoDocumentoEntity tipoDoc = tipoDocumentoRepository.findById(requestDto.getCodigoTipoDocumento())
                    .orElseThrow(() -> new BusinessException("Codigo de tipo de documento no encontrado con ID: " + requestDto.getCodigoTipoDocumento()));

            //Se envia correo con la primera clave
            emailService.enviarClaveUsuario(
                    requestDto.getCorreo(),
                    tipoDoc.getAbreviatura(),
                    requestDto.getNumeroDocumento(),
                    claveTemporal);

            return usuarioMapper.entityToResponseDto(usuarioEntity);
        } catch (DataIntegrityViolationException | PersistenceException ex) {
            throw new BusinessException("Problemas en el registro, contáctese con sistemas.");
        }
    }

    @Transactional
    public UsuarioResponseDto actualizar(UsuarioRequestDto requestDto, HttpServletRequest request) {
        try {
            UsuarioEntity usuarioEntity = getUsuarioEntity(requestDto.getId());

            usuarioMapper.update(usuarioEntity, requestDto);

            setModAuditFields(usuarioEntity, request);

            usuarioRepository.save(usuarioEntity);

            return usuarioMapper.entityToResponseDto(usuarioEntity);
        } catch (DataIntegrityViolationException | PersistenceException ex) {
            throw new BusinessException("Problemas en el registro, contáctese con sistemas.");
        }
    }

    @Transactional
    public UsuarioResponseDto eliminar(Long id, HttpServletRequest request) {
        UsuarioEntity usuarioEntity = getUsuarioEntity(id);

        usuarioEntity.setEstadoRegistro(Constante.ESTADO_INACTIVO);
        setModAuditFields(usuarioEntity, request);
        usuarioRepository.save(usuarioEntity);

        return usuarioMapper.entityToResponseDto(usuarioEntity);
    }

    public UsuarioResponseDto getUsuario(Long id) {
        UsuarioEntity usuarioEntity = getUsuarioEntity(id);

        return usuarioMapper.entityToResponseDto(usuarioEntity);
    }

    @Transactional
    public UsuarioResponseDto actualizarDatosUsuarioEnPerfil(UsuarioRequestDto requestDto, HttpServletRequest request) {
        try {
            UsuarioEntity usuarioEntity = getUsuarioEntity(requestDto.getId());

            usuarioEntity.setNombres(requestDto.getNombres());
            usuarioEntity.setApePaterno(requestDto.getApePaterno());
            usuarioEntity.setApeMaterno(requestDto.getApeMaterno());
            usuarioEntity.setCorreo(requestDto.getCorreo());
            usuarioEntity.setTelefono(requestDto.getTelefono());

            setModAuditFields(usuarioEntity, request);

            usuarioRepository.save(usuarioEntity);

            return usuarioMapper.entityToResponseDto(usuarioEntity);
        } catch (DataIntegrityViolationException | PersistenceException ex) {
            throw new BusinessException("Problemas en el registro, contáctese con sistemas.");
        }
    }

    public UsuarioResponseDto resetearPassword(UsuarioResetPasswordRequetDto req, HttpServletRequest request) {
        try {
            UsuarioEntity usuarioEntity = usuarioRepository.findById(req.getId()).orElseThrow(() -> new NotFoundException("Usuario no encontrado o desactivado"));
            usuarioEntity.setPassword(passwordEncoder.encode(req.getPassword()));

            setModAuditFields(usuarioEntity, request);

            usuarioRepository.save(usuarioEntity);

            return usuarioMapper.entityToResponseDto(usuarioEntity);
        } catch (DataIntegrityViolationException | PersistenceException ex) {
            throw new BusinessException("Problemas en el registro, contáctese con sistemas.");
        }
    }

    /**
     * Se usa para obtener la entidad de un usuario
     * @param codigo identificador único
     * @return {@link UsuarioEntity}
     */
    private UsuarioEntity getUsuarioEntity(Long codigo) {
        return usuarioRepository.findById(codigo).orElseThrow(
                () -> new NotFoundException("Usuario con código " + codigo + " no encontrado")
        );
    }
}
