package com.raissa.payments.service.operativo.solicitud.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.commons.RestResponsePage;
import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.dto.commons.InstitucionFinancieraDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.LiquidacionSolicitudConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.LiquidacionSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionFlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.AbonoSolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.CargoSolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.LiquidacionSolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.ObservacionResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.TrackingResponseDto;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.entity.commons.InstitucionFinancieraEntity;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.domain.repository.commons.InstitucionFinancieraRepository;
import com.raissa.payments.exception.commons.ResponseApiException;
import com.raissa.payments.service.administrativo.general.ClienteDataSourceService;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SolicitudServiceImpl extends AbstractRaissaPaymentsService implements SolicitudService {
    private final ClienteDataSourceService clienteDataSourceService;
    private final InstitucionFinancieraRepository institucionFinancieraRepository;
    private final UsuarioRepository usuarioRepository;

    private final RestTemplate restTemplate;

    @Value("${conector.api.url}")
    private String urlConectorServer;

    public SolicitudSearchResponseDto getFindSolicitudesPage(SolicitudSearchDto search) {
        SolicitudSearchConnectDto searchConnect = getSolicitudSearchConnectDto(search);

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/flujo-solicitud/list-page-solicitud").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(search.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SolicitudSearchConnectDto> requestEntity = new HttpEntity<>(searchConnect, headers);

        ResponseEntity<RestResponsePage<SolicitudConnectResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        List<SolicitudConnectResponseDto> content = response.getBody().getContent()
                    .stream()
                    .toList();
        return new SolicitudSearchResponseDto(response.getBody().getTotalPages(),
                response.getBody().getTotalElements(),
                response.getBody().getNumber(),
                response.getBody().getSize(),
                cargarDatosAdicionales(content));
    }

    public Long flujoSolicitud(FlujoSolicitudRequestDto flujo, HttpServletRequest request){
        FlujoSolicitudConnectRequestDto flujoConnect = new FlujoSolicitudConnectRequestDto();
        flujoConnect.setSolicitudId(flujo.getIdSolicitud());
        flujoConnect.setUsuario(getCurrentUser());
        flujoConnect.setUsuarioAuditoria(getCurrentUser());
        flujoConnect.setFechaAuditoria(LocalDateTime.now());
        flujoConnect.setTerminalAuditoria(request.getRemoteHost());
        flujoConnect.setIpAuditoria(request.getRemoteAddr());

        if(flujo.getFlujo().equals("ejecutar") || flujo.getFlujo().equals("validar") ){
            List<InstitucionFinancieraEntity> instituciones = institucionFinancieraRepository.findByEstadoRegistro(Constante.ESTADO_ACTIVO);

            List<InstitucionFinancieraDto> dtos = instituciones.stream()
                    .filter(Objects::nonNull)
                    .map(e -> InstitucionFinancieraDto.builder()
                            .codigo(e.getCodigo())
                            .codigoSbs(e.getCodigoSbs())
                            .build())
                    .toList();

            flujoConnect.setListInstituciones(dtos);
        }

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/flujo-solicitud/" + flujo.getFlujo())
                    .toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(flujo.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FlujoSolicitudConnectRequestDto> requestEntity = new HttpEntity<>(flujoConnect, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        return response.getBody();
    }

    public List<TrackingResponseDto> listTracking(TrackingRequestDto dto) {
        TrackingConnectRequestDto requestConnect = new TrackingConnectRequestDto();
        requestConnect.setIdSolicitud(dto.getIdSolicitud());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/tracking/list-tracking").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(dto.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TrackingConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<List<TrackingResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        return completarDatosTrack(response.getBody());
    }

    public Long flujoSolicitudObservacion(ObservacionFlujoSolicitudRequestDto dto, HttpServletRequest request) {
        ObservacionConnectRequestDto requestConnect = new ObservacionConnectRequestDto();
        requestConnect.setSolicitudId(dto.getIdSolicitud());
        requestConnect.setDescripcionObservacion(dto.getDescripcionObservacion());
        requestConnect.setUsuarioObservacion(getCurrentUser());
        requestConnect.setUsuario(getCurrentUser());
        requestConnect.setEventoObservacion(dto.getEventoObservacion());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/flujo-solicitud/" + dto.getFlujo())
                .toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(dto.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ObservacionConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        return response.getBody();
    }

    public List<ObservacionResponseDto> listObservacion(ObservacionRequestDto dto) {
        ObservacionConnectRequestDto requestConnect = new ObservacionConnectRequestDto();
        requestConnect.setSolicitudId(dto.getSolicitudId());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/observaciones/list").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(dto.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ObservacionConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<List<ObservacionResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        return response.getBody();
    }

    public LiquidacionSolicitudResponseDto getResumenLiquidacion(LiquidacionSolicitudRequestDto req) {
        LiquidacionSolicitudConnectRequestDto connect = new LiquidacionSolicitudConnectRequestDto();
        connect.setSolicitudId(req.getSolicitudId());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/flujo-solicitud/resumen-liquidacion").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(req.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LiquidacionSolicitudConnectRequestDto> requestEntity = new HttpEntity<>(connect, headers);

        ResponseEntity<LiquidacionSolicitudResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        return response.getBody();
    }

    private List<SolicitudResponseDto> cargarDatosAdicionales(List<SolicitudConnectResponseDto> listConnectResponse) {
        return listConnectResponse.stream().map(connectResponse -> {
            SolicitudResponseDto solicitud = new SolicitudResponseDto();
            BeanUtils.copyProperties(connectResponse, solicitud);

            Optional<UsuarioEntity> usuarioCarga =
                    usuarioRepository.findByUsernameAndEstadoRegistro(
                            connectResponse.getUsuarioCarga(),
                            Constante.ESTADO_ACTIVO
                    );

            if (usuarioCarga.isPresent()) {
                solicitud.setNombreUsuarioCarga(
                        formatearNombre(
                                usuarioCarga.get().getNombres(),
                                usuarioCarga.get().getApePaterno()
                        )
                );
            } else {
                solicitud.setNombreUsuarioCarga(connectResponse.getUsuarioCarga());
            }

            StringBuilder nombreUsuariosAutorizacion = new StringBuilder();
            if (connectResponse.getUsuariosAutorizacion() != null && !connectResponse.getUsuariosAutorizacion().isEmpty()) {
                for (String usuarioAutorizacion : connectResponse.getUsuariosAutorizacion()) {
                    Optional<UsuarioEntity> usuarioAutorizacionOpt =
                            usuarioRepository.findByUsernameAndEstadoRegistro(
                                    usuarioAutorizacion,
                                    Constante.ESTADO_ACTIVO
                            );

                    usuarioAutorizacionOpt.ifPresent(usuarioEntity -> nombreUsuariosAutorizacion.append(formatearNombre(
                                    usuarioEntity.getNombres(),
                                    usuarioEntity.getApePaterno()
                            )
                    ).append(" - "));
                }
            }
            solicitud.setNombresUsuariosAutorizacion(nombreUsuariosAutorizacion.toString());

            List<CargoSolicitudResponseDto> cargos = connectResponse.getCargos().stream().map(cargo -> {
                CargoSolicitudResponseDto cargoDto = new CargoSolicitudResponseDto();
                BeanUtils.copyProperties(cargo, cargoDto);

                InstitucionFinancieraEntity bancoCargo =
                        institucionFinancieraRepository.findByCodigoAndEstadoRegistro(
                                cargo.getCodigoEntidadFinanciera(),
                                Constante.ESTADO_ACTIVO
                        );

                cargoDto.setNombreEntidadFinanciera(bancoCargo.getAbreviatura());

                List<AbonoSolicitudResponseDto> abonos = cargo.getAbonos().stream().map(abono -> {
                    AbonoSolicitudResponseDto abonoDto = new AbonoSolicitudResponseDto();
                    BeanUtils.copyProperties(abono, abonoDto);
                    InstitucionFinancieraEntity bancoAbono =
                            institucionFinancieraRepository.findByCodigoAndEstadoRegistro(
                                    abono.getCodigoEntidadFinanciera(),
                                    Constante.ESTADO_ACTIVO
                            );
                    abonoDto.setNombreEntidadFinanciera(bancoAbono.getAbreviatura());
                    return abonoDto;
                }).toList();
                cargoDto.setAbonos(abonos);

                return cargoDto;
            }).toList();
            solicitud.setCargos(cargos);

            return solicitud;
        }).toList();
    }

    private static SolicitudSearchConnectDto getSolicitudSearchConnectDto(SolicitudSearchDto search) {
        SolicitudSearchConnectDto searchConnect = new SolicitudSearchConnectDto();
        searchConnect.setUsuario(search.getUsuario());
        searchConnect.setFechaInicial(search.getFechaInicial());
        searchConnect.setFechaFinal(search.getFechaFinal());
        searchConnect.setCodigo(search.getCodigo());
        searchConnect.setEstadoSolicitud(search.getEstadoSolicitud());
        searchConnect.setSize(search.getSize());
        searchConnect.setSortField(search.getSortField());
        searchConnect.setSortOrder(search.getSortOrder());
        searchConnect.setPage(search.getPage());
        return searchConnect;
    }

    private List<TrackingResponseDto> completarDatosTrack(List<TrackingResponseDto> trackings){
        if (trackings == null || trackings.isEmpty()) {
            return new ArrayList<>();
        }
        // Extraer todos los nombres de usuario únicos de los trackings
        List<String> usernames = trackings.stream()
                .map(TrackingResponseDto::getUsuario)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (usernames.isEmpty()) {
            return trackings;
        }

        // Buscar todos los usuarios activos por sus usernames
        List<UsuarioEntity> usuarios = usuarioRepository.findByUsernameInAndEstadoRegistro(
                usernames,
                Constante.ESTADO_ACTIVO
        );

        // Crear un mapa para búsqueda rápida username -> nombre de usuario
        Map<String, String> nombreUsuarioMap = usuarios.stream()
                .collect(Collectors.toMap(
                        UsuarioEntity::getUsername,
                        usuario -> formatearNombre(usuario.getNombres(), usuario.getApePaterno()),
                        (existing, replacement) -> existing
                ));

        // Completar el nombreUsuario en cada tracking
        trackings.forEach(tracking -> {
            String nombreCompleto = nombreUsuarioMap.get(tracking.getUsuario());
            tracking.setNombreUsuario(nombreCompleto != null ? nombreCompleto : tracking.getUsuario());
        });

        return trackings;
    }
}