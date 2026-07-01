package com.raissa.payments.service.administrativo.perfil.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.administrativo.request.PerfilRequestDto;
import com.raissa.payments.domain.dto.administrativo.request.PerfilSearchDto;
import com.raissa.payments.domain.dto.administrativo.response.PerfilResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.PerfilSearchResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioSearchResponseDto;
import com.raissa.payments.domain.entity.administrativo.ClienteEntity;
import com.raissa.payments.domain.entity.administrativo.PerfilClienteEntity;
import com.raissa.payments.domain.entity.administrativo.PerfilEntity;
import com.raissa.payments.domain.entity.administrativo.PerfilSistemaEntity;
import com.raissa.payments.domain.entity.administrativo.SistemaEntity;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.mappers.administrativo.PerfilMapper;
import com.raissa.payments.domain.repository.administrativo.ClienteRepository;
import com.raissa.payments.domain.repository.administrativo.PerfilClienteRepository;
import com.raissa.payments.domain.repository.administrativo.PerfilRepository;
import com.raissa.payments.domain.repository.administrativo.PerfilSistemaRepository;
import com.raissa.payments.domain.repository.administrativo.SistemaRepository;
import com.raissa.payments.exception.commons.BusinessException;
import com.raissa.payments.exception.commons.NotFoundException;
import com.raissa.payments.service.administrativo.perfil.PerfilService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerfilServiceImpl extends AbstractRaissaPaymentsService implements PerfilService {
    private final PerfilRepository perfilRepository;
    private final PerfilClienteRepository perfilClienteRepository;
    private final ClienteRepository clienteRepository;
    private final SistemaRepository sistemaRepository;
    private final PerfilSistemaRepository perfilSistemaRepository;

    //Mappers
    private final PerfilMapper perfilMapper;

    @Value("${raissa.sistema.codigo}")
    private String codigoSistema;

    @Transactional
    public PerfilResponseDto registrar(PerfilRequestDto requestDto, HttpServletRequest request) {
        // 1. Validar requestDto
        if (requestDto == null) {
            throw new BusinessException("La solicitud de registro no puede ser nula");
        }

        if (requestDto.getCodigoCliente() == null) {
            throw new BusinessException("El código de cliente es obligatorio");
        }

        // 2. Verificar si ya existe un perfil con el mismo nombre
        if (perfilRepository.existsByDescripcion(requestDto.getDescripcion())) {
            throw new BusinessException("Ya existe un perfil con el nombre: " + requestDto.getDescripcion());
        }

        try {
            // 3. Obtener cliente (con manejo de Optional)
            ClienteEntity cliente = clienteRepository.findById(requestDto.getCodigoCliente())
                    .orElseThrow(() -> new BusinessException("Cliente no encontrado con ID: " + requestDto.getCodigoCliente()));

            // 4. Mapear y guardar perfil
            PerfilEntity perfilEntity = perfilMapper.dtoToEntity(requestDto);
            setInsAuditFields(perfilEntity, request);
            PerfilEntity savedPerfil = perfilRepository.save(perfilEntity);

            // 5. Crear y guardar relación perfil-cliente
            PerfilClienteEntity perfilCliente = createPerfilClienteEntity(savedPerfil, cliente, request);
            perfilClienteRepository.save(perfilCliente);

            // 5. Crear y guardar relación perfil-sistema
            SistemaEntity sistema = sistemaRepository.findById(codigoSistema)
                    .orElseThrow(() -> new BusinessException("Sistema no encontrado con ID: " + codigoSistema));
            PerfilSistemaEntity perfilSistema = createPerfilSistemaEntity(savedPerfil, sistema, request);
            perfilSistemaRepository.save(perfilSistema);

            // 6. Retornar respuesta
            return perfilMapper.entityToResponseDto(savedPerfil);

        } catch (DataIntegrityViolationException e) {
            log.error("Violación de integridad de datos al registrar perfil: {}", e.getMessage(), e);
            throw new BusinessException("El perfil ya existe o hay datos duplicados");
        } catch (PersistenceException e) {
            log.error("Error de persistencia al registrar perfil: {}", e.getMessage(), e);
            throw new BusinessException("Error al guardar el perfil en la base de datos");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al registrar perfil: {}", e.getMessage(), e);
            throw new BusinessException("Error interno al procesar el registro");
        }
    }

    @Transactional
    public PerfilResponseDto actualizar(PerfilRequestDto requestDto, HttpServletRequest request) {
        try {
            PerfilEntity perfilEntity = getPerfilEntity(requestDto.getCodigo());

            perfilMapper.update(perfilEntity, requestDto);

            setModAuditFields(perfilEntity, request);

            perfilRepository.save(perfilEntity);

            return perfilMapper.entityToResponseDto(perfilEntity);
        } catch (DataIntegrityViolationException | PersistenceException ex) {
            throw new BusinessException("Problemas en el registro, contáctese con sistemas.");
        }
    }

    @Transactional
    public PerfilResponseDto eliminar(Long id, HttpServletRequest request) {
        PerfilEntity perfilEntity = getPerfilEntity(id);

        if(id != null) {
            throw new BusinessException("El perfil no existe");
        }
        perfilEntity.setEstadoRegistro(Constante.ESTADO_INACTIVO);
        perfilRepository.save(perfilEntity);

        return perfilMapper.entityToResponseDto(perfilEntity);
        //TODO completar esta eliminacion logica, dando de baja a todas las relaciones de perfil, perfil-usuario, perfil-sistema, perfil-cliente, menu-perfil
    }

    public PerfilResponseDto getPerfil(Long id) {
        PerfilEntity entity = getPerfilEntity(id);

        return perfilMapper.entityToResponseDto(entity);
    }

    public PerfilSearchResponseDto getPerfilesPage(PerfilSearchDto perfilSearch) {
        Pageable pageable = buildPageable(perfilSearch);

        Page<PerfilEntity> pagePerfilEntity = perfilRepository.searchByEmpresa(perfilSearch.getEstadoRegistro(),
                perfilSearch.getDescripcion(),
                perfilSearch.getAbreviatura(),
                perfilSearch.getCodigoCliente(),
                codigoSistema,
                pageable);

        List<PerfilResponseDto> list = pagePerfilEntity.getContent()
                .stream()
                .map(perfilMapper::entityToResponseDto)
                .toList();

        return new PerfilSearchResponseDto(
                pagePerfilEntity.getTotalPages(),
                pagePerfilEntity.getTotalElements(),
                pagePerfilEntity.getNumber(),
                pagePerfilEntity.getSize(),
                list
        );
    }

    /**
     * Se usa para obtener la entidad de un perfil
     * @param codigo identificador único
     * @return {@link PerfilEntity}
     */
    private PerfilEntity getPerfilEntity(Long codigo) {
        return perfilRepository.findById(codigo).orElseThrow(
                () -> new NotFoundException("Perfil con código " + codigo + " no encontrado")
        );
    }

    /**
     * Registra vínculo entre perfil y cliente
     * @param perfil Perfil
     * @param cliente Cliente/empresa
     * @param request datos de la peticion
     * @return {@link PerfilClienteEntity}
     */
    private PerfilClienteEntity createPerfilClienteEntity(PerfilEntity perfil, ClienteEntity cliente, HttpServletRequest request) {
        PerfilClienteEntity perfilCliente = new PerfilClienteEntity();
        perfilCliente.setPerfil(perfil);
        perfilCliente.setCliente(cliente);
        setInsAuditFields(perfilCliente, request);
        return perfilCliente;
    }

    /**
     * Registra vínculo entre perfil y sistema
     * @param perfil Perfil
     * @param sistema Sistema
     * @param request datos de la peticion
     * @return {@link PerfilSistemaEntity}
     */
    private PerfilSistemaEntity createPerfilSistemaEntity(PerfilEntity perfil, SistemaEntity sistema, HttpServletRequest request) {
        PerfilSistemaEntity perfilSistema = new PerfilSistemaEntity();
        perfilSistema.setPerfil(perfil);
        perfilSistema.setSistema(sistema);
        setInsAuditFields(perfilSistema, request);
        return perfilSistema;
    }
}