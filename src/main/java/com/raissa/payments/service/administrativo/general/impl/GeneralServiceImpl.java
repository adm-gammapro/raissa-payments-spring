package com.raissa.payments.service.administrativo.general.impl;

import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;
import com.raissa.comun.util.Constante;
import com.raissa.comun.util.ConstanteError;
import com.raissa.payments.domain.dto.commons.EstadoSolicitudDto;
import com.raissa.payments.domain.dto.commons.TipoDocumentoResponseDto;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.commons.EstadoSolicitudEntity;
import com.raissa.payments.domain.entity.commons.InstitucionFinancieraEntity;
import com.raissa.payments.domain.entity.commons.TipoDocumentoEntity;
import com.raissa.payments.domain.mappers.commons.EstadoSolicitudMapper;
import com.raissa.payments.domain.mappers.commons.InstitucionFinancieraMapper;
import com.raissa.payments.domain.mappers.commons.TipoDocumentoMapper;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.domain.repository.commons.EstadoSolicitudRepository;
import com.raissa.payments.domain.repository.commons.InstitucionFinancieraRepository;
import com.raissa.payments.domain.repository.commons.TipoDocumentoRepository;
import com.raissa.payments.service.administrativo.general.GeneralService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import com.raissa.payments.util.EmailService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GeneralServiceImpl extends AbstractRaissaPaymentsService implements GeneralService {
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    //Repositorios
    private final InstitucionFinancieraRepository institucionFinancieraRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstadoSolicitudRepository estadoSolicitudRepository;
    private final UsuarioRepository usuarioRepository;

    //Mappers
    private final InstitucionFinancieraMapper institucionFinancieraMapper;
    private final TipoDocumentoMapper tipoDocumentoMapper;
    private final EstadoSolicitudMapper estadoSolicitudMapper;

    // Cachés en memoria
    private List<InstitucionFinancieraResponseDto> institucionesFinancierasCache;
    private List<TipoDocumentoResponseDto> tiposDocumentoCache;
    private List<EstadoSolicitudDto> estadosSolicitudCache;

    private boolean isCacheLoaded = false;

    /**
     * Inicializa la caché al levantar el proyecto
     */
    @PostConstruct
    public void init() {
        log.info("========== INICIANDO SISTEMA DE CACHÉ ==========");
        cargarTodasLasCaches();
        log.info("========== SISTEMA DE CACHÉ INICIALIZADO ==========");
    }

    public String obtenerCodigoRandom(String username, String passwordPlano, String modoEnvio) {
        Optional<UsuarioEntity> optUser = usuarioRepository.findByUsernameAndEstadoRegistro(username, Constante.ESTADO_ACTIVO);

        if (optUser.isEmpty() || passwordPlano.isEmpty()) {
            return "";
        }

        UsuarioEntity user = optUser.get();

        if(passwordEncoder.matches(passwordPlano, user.getPassword())) {
            String codigo = generarCodigo(6);
            if (modoEnvio.equals("email")) {
                emailService.enviarCodigo(user.getCorreo(), codigo);
                return codigo;
            }

            return "-";
        } else {
            return "";
        }
    }

    /**
     * Obtiene lista de instituciones financieras DESDE CACHÉ
     */
    public List<InstitucionFinancieraResponseDto> listarInstitucionFinanciera() {
        log.debug("Consultando instituciones financieras desde CACHÉ");

        if (!isCacheLoaded || institucionesFinancierasCache == null) {
            log.warn(ConstanteError.MENSAJE_ERROR_CACHE_DATA);
            cargarCacheInstitucionesFinancieras();
        }

        // Retornar una copia para evitar modificaciones externas
        return new ArrayList<>(institucionesFinancierasCache);
    }

    /**
     * Obtiene lista de tipos de documento DESDE CACHÉ
     */
    @Override
    public List<TipoDocumentoResponseDto> listarTipoDocumento() {
        log.debug("Consultando tipos de documento desde CACHÉ");

        if (!isCacheLoaded || tiposDocumentoCache == null) {
            log.warn(ConstanteError.MENSAJE_ERROR_CACHE_DATA);
            cargarCacheTiposDocumento();
        }

        return new ArrayList<>(tiposDocumentoCache);
    }

    /**
     * Obtiene lista de estados de solicitud DESDE CACHÉ
     */
    public List<EstadoSolicitudDto> listarEstadosActivos() {
        log.debug("Consultando estados de solicitud desde CACHÉ");

        if (!isCacheLoaded || estadosSolicitudCache == null) {
            log.warn(ConstanteError.MENSAJE_ERROR_CACHE_DATA);
            cargarCacheEstadosSolicitud();
        }

        return new ArrayList<>(estadosSolicitudCache);
    }

    /**
     * Obtiene el nombre de una institución financiera por su código
     */
    public String obtenerNombreInstitucionFinanciera(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            return "";
        }

        List<InstitucionFinancieraResponseDto> instituciones = listarInstitucionFinanciera();

        return instituciones.stream()
                .filter(i -> codigo.equals(i.getCodigo()))
                .map(InstitucionFinancieraResponseDto::getNombre)
                .findFirst()
                .orElse(codigo); // Retorna el código si no encuentra el nombre
    }

    /**
     * Obtiene el nombre de una institución financiera por su código SBS
     */
    public String obtenerNombreInstitucionPorCodigoSbs(String codigoSbs) {
        if (codigoSbs == null || codigoSbs.isEmpty()) {
            return "";
        }

        List<InstitucionFinancieraResponseDto> instituciones = listarInstitucionFinanciera();

        return instituciones.stream()
                .filter(i -> codigoSbs.equals(i.getCodigoSbs()))
                .map(InstitucionFinancieraResponseDto::getNombre)
                .findFirst()
                .orElse(codigoSbs);
    }

    /**
     * Obtiene la institución financiera completa por su código
     */
    public InstitucionFinancieraResponseDto obtenerInstitucionPorCodigo(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            return null;
        }

        List<InstitucionFinancieraResponseDto> instituciones = listarInstitucionFinanciera();

        return instituciones.stream()
                .filter(i -> codigo.equals(i.getCodigo()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Carga todas las caches al iniciar
     */
    private void cargarTodasLasCaches() {
        cargarCacheInstitucionesFinancieras();
        cargarCacheTiposDocumento();
        cargarCacheEstadosSolicitud();
        isCacheLoaded = true;
        log.info("✅ Todas las caches han sido cargadas exitosamente");
    }

    /**
     * Carga la caché de instituciones financieras
     */
    private void cargarCacheInstitucionesFinancieras() {
        try {
            log.info("Cargando caché de instituciones financieras...");
            List<InstitucionFinancieraEntity> entities = institucionFinancieraRepository.findAll();
            institucionesFinancierasCache = entities.stream()
                    .map(institucionFinancieraMapper::entityToResponseDto)
                    .toList();
            log.info("✅ Caché de instituciones financieras cargada: {} registros", institucionesFinancierasCache.size());
        } catch (Exception e) {
            log.error("Error al cargar caché de instituciones financieras: {}", e.getMessage(), e);
            institucionesFinancierasCache = Collections.emptyList();
        }
    }

    /**
     * Carga la caché de tipos de documento
     */
    private void cargarCacheTiposDocumento() {
        try {
            log.info("Cargando caché de tipos de documento...");
            List<TipoDocumentoEntity> entities = tipoDocumentoRepository.findByEstadoRegistro(Constante.ESTADO_ACTIVO);
            tiposDocumentoCache = entities.stream()
                    .map(tipoDocumentoMapper::entityToResponseDto)
                    .toList();
            log.info("✅ Caché de tipos de documento cargada: {} registros", tiposDocumentoCache.size());
        } catch (Exception e) {
            log.error("Error al cargar caché de tipos de documento: {}", e.getMessage(), e);
            tiposDocumentoCache = Collections.emptyList();
        }
    }

    /**
     * Carga la caché de estados de solicitud
     */
    private void cargarCacheEstadosSolicitud() {
        try {
            log.info("Cargando caché de estados de solicitud...");
            List<EstadoSolicitudEntity> entities = estadoSolicitudRepository
                    .findByEstadoRegistroOrderByDescripcionAsc(Constante.ESTADO_ACTIVO);
            estadosSolicitudCache = entities.stream()
                    .map(estadoSolicitudMapper::entityToResponseDto)
                    .toList();
            log.info("✅ Caché de estados de solicitud cargada: {} registros", estadosSolicitudCache.size());
        } catch (Exception e) {
            log.error("Error al cargar caché de estados de solicitud: {}", e.getMessage(), e);
            estadosSolicitudCache = Collections.emptyList();
        }
    }
}