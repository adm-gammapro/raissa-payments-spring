package com.raissa.payments.service.operativo.solicitud.impl;

import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;
import com.raissa.comun.util.Constante;
import com.raissa.payments.commons.RestResponsePage;
import com.raissa.payments.configuracion.rabbit.RabbitMessageSender;
import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.dto.commons.InstitucionFinancieraDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConsultaVoucherAbonoSolicitudConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConsultaVoucherAbonoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ConstanciaPagoResponse;
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
import com.raissa.payments.service.administrativo.general.GeneralService;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import com.raissa.payments.util.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SolicitudServiceImpl extends AbstractRaissaPaymentsService implements SolicitudService {
    private final ClienteDataSourceService clienteDataSourceService;
    private final InstitucionFinancieraRepository institucionFinancieraRepository;
    private final UsuarioRepository usuarioRepository;

    private final RestTemplate restTemplate;
    private final RabbitMessageSender rabbitMessageSender;
    private final EmailService emailService;
    private final GeneralService generalService;

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
        // Validar campos obligatorios
        if (flujo.getIdSolicitud() == null) {
            throw new ResponseApiException("El id de solicitud es obligatorio");
        }
        if (flujo.getCodigoCliente() == null) {
            throw new ResponseApiException("El código de cliente es obligatorio");
        }

        FlujoSolicitudConnectRequestDto flujoConnect = construirDtoBase(flujo, request);

        if (flujo.getFlujo().equals("ejecutar") || flujo.getFlujo().equals("validar")) {
            String urlProcesamiento = UriComponentsBuilder.fromUriString(urlConectorServer + "/flujo-solicitud/en-procesamiento")
                    .toUriString();

            ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(flujo.getCodigoCliente());

            HttpHeaders headers = new HttpHeaders();
            headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<FlujoSolicitudConnectRequestDto> requestEntity = new HttpEntity<>(flujoConnect, headers);

            ResponseEntity<Long> response = restTemplate.exchange(
                    urlProcesamiento,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ResponseApiException("Error al llamar al API para hacer cambio de indicador a en procesamiento");
            }

            // Agregar lista de instituciones
            List<InstitucionFinancieraDto> instituciones = obtenerInstitucionesActivas();
            flujoConnect.setListInstituciones(instituciones);

            if (flujo.getFlujo().equals("validar")) {
                rabbitMessageSender.enviarValidacionSolicitud(flujoConnect);
                log.info("✅ Validación encolada - SolicitudId: {}", flujo.getIdSolicitud());
            } else {
                rabbitMessageSender.enviarEjecucionSolicitud(flujoConnect);
                log.info("✅ Ejecución encolada - SolicitudId: {}", flujo.getIdSolicitud());
            }

            return flujo.getIdSolicitud();
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

    @Override
    public ConstanciaPagoResponse obtenerConstanciaPago(ConsultaVoucherAbonoSolicitudRequestDto req) {
        log.info("=== INICIANDO OBTENCIÓN DE CONSTANCIA DE PAGO ===");
        log.info("AbonoSolicitudId: {}, CodigoCliente: {}", req.getAbonoSolicitudId(), req.getCodigoCliente());

        // 1. Crear DTO para el conector
        ConsultaVoucherAbonoSolicitudConnectRequestDto connect = new ConsultaVoucherAbonoSolicitudConnectRequestDto();
        connect.setAbonoSolicitudId(req.getAbonoSolicitudId());

        // 2. Construir URL
        String url = UriComponentsBuilder
                .fromUriString(urlConectorServer + "/flujo-solicitud/constancia-pago")
                .toUriString();

        log.debug("URL del servicio: {}", url);

        // 3. Obtener DataSource del cliente
        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(req.getCodigoCliente());
        log.debug("DataSource obtenido: {}", clienteDataSource.getCodigoDataSource());

        // 4. Configurar headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 5. Crear request entity
        HttpEntity<ConsultaVoucherAbonoSolicitudConnectRequestDto> requestEntity = new HttpEntity<>(connect, headers);

        // 6. Realizar la llamada al servicio
        ResponseEntity<ConstanciaPagoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        // 7. Validar respuesta
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Error al llamar al API de constancia de pago - StatusCode: {}", response.getStatusCode());
            throw new ResponseApiException("Error al llamar al API para obtener constancia de pago");
        }

        ConstanciaPagoResponse constancia = response.getBody();
        log.info("Constancia de pago obtenida exitosamente para operación: {}", constancia.getCodigoOperacion());

        return completarDatos(constancia);
    }

    @Override
    public byte[] generarConstanciaPagoPDF(ConsultaVoucherAbonoSolicitudRequestDto req) {
        ConstanciaPagoResponse constancia = obtenerConstanciaPago(req);
        String html = construirHTMLConstancia(constancia);
        return convertirHtmlAPdf(html);
    }

    @Override
    public void enviarConstanciaPagoCorreo(ConsultaVoucherAbonoSolicitudRequestDto req) {
        try {
            ConstanciaPagoResponse constancia = obtenerConstanciaPago(req);

            emailService.enviarConstancia(req.getCorreoDestino(),
                    constancia);
            log.info("Constancia de pago enviada a: {}", req.getCorreoDestino());

        } catch (MessagingException e) {
            log.error("Error al enviar constancia de pago a: {}", req.getCorreoDestino(), e);
            throw new RuntimeException("Error al enviar constancia de pago", e);
        }
    }

    /**
     * Construir HTML con el mismo diseño del frontend
     */
    private String construirHTMLConstancia(ConstanciaPagoResponse constancia) {
        return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Constancia de Pago</title>
            <style type="text/css">
                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    margin: 0;
                    padding: 20px;
                }
                .constancia-card {
                    max-width: 600px;
                    margin: 0 auto;
                    background: white;
                    border-radius: 12px;
                    overflow: hidden;
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
                }
                .interbank-header {
                    background: #0d47a1;
                    padding: 16px 20px;
                    border-bottom: 3px solid #00c853;
                }
                .interbank-header h1 {
                    color: white;
                    font-size: 1.5rem;
                    font-weight: 700;
                    margin: 0;
                }
                .titulo-constancia {
                    background: #e8f0fe;
                    padding: 12px 20px;
                    border-bottom: 1px solid #d0e0f0;
                }
                .titulo-constancia h3 {
                    color: #0d47a1;
                    font-size: 1rem;
                    font-weight: 600;
                    margin: 0;
                }
                .fecha-hora {
                    padding: 12px 20px;
                    background: #f1f8e9;
                    color: #2e7d32;
                    font-size: 0.875rem;
                    font-weight: 500;
                    border-bottom: 1px solid #c8e6c9;
                }
                .info-grid {
                    padding: 20px;
                }
                .info-row {
                    display: flex;
                    justify-content: space-between;
                    align-items: flex-start;
                    padding: 8px 0;
                    border-bottom: 1px solid #e2e8f0;
                }
                .info-label {
                    font-weight: 600;
                    color: #334155;
                    font-size: 0.875rem;
                    min-width: 140px;
                }
                .info-value {
                    color: #0f172a;
                    font-size: 0.875rem;
                    text-align: right;
                    font-weight: 500;
                }
                .monto-row {
                    border-bottom: none;
                    margin-top: 8px;
                    padding-top: 16px;
                }
                .monto-label {
                    font-weight: 700;
                    color: #0d47a1;
                }
                .monto-valor {
                    font-size: 1.125rem;
                    font-weight: 700;
                    color: #00c853;
                }
            </style>
        </head>
        <body>
            <div class="constancia-card">
                <div class="interbank-header">
                    <h1>Raissa Open Banking</h1>
                </div>
                <div class="titulo-constancia">
                    <h3>Constancia de transferencia de fondos</h3>
                </div>
                <div class="fecha-hora">
                    %s
                </div>
                <div class="info-grid">
                    <div class="info-row">
                        <span class="info-label">Código de operación:</span>
                        <span class="info-value">%s</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Banco de origen:</span>
                        <span class="info-value">%s</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Cuenta cargo:</span>
                        <span class="info-value">%s</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Destinatario:</span>
                        <span class="info-value">%s</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Destino:</span>
                        <span class="info-value">%s<br/>%s</span>
                    </div>
                    <div class="info-row monto-row">
                        <span class="info-label monto-label">Moneda y monto:</span>
                        <span class="info-value monto-valor">%s</span>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                nvl(constancia.getFecha()),
                nvl(constancia.getCodigoOperacion()),
                nvl(constancia.getBancoOrigen()),
                nvl(constancia.getCuentaOrigen()),
                nvl(constancia.getDestinatario()),
                nvl(constancia.getEntidadDestino()),
                nvl(constancia.getDestino()),
                nvl(constancia.getMonto())
        );
    }

    /**
     * Convertir HTML a PDF usando Flying Saucer (OpenPDF)
     */
    private byte[] convertirHtmlAPdf(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Error al generar PDF", e);
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    private FlujoSolicitudConnectRequestDto construirDtoBase(FlujoSolicitudRequestDto flujo, HttpServletRequest request) {
        FlujoSolicitudConnectRequestDto dto = new FlujoSolicitudConnectRequestDto();
        dto.setSolicitudId(flujo.getIdSolicitud());
        dto.setCodigoCliente(flujo.getCodigoCliente());
        dto.setUsuario(getCurrentUser());
        dto.setUsuarioAuditoria(getCurrentUser());
        dto.setTerminalAuditoria(request.getRemoteHost());
        dto.setIpAuditoria(request.getRemoteAddr());
        return dto;
    }

    private List<InstitucionFinancieraDto> obtenerInstitucionesActivas() {
        List<InstitucionFinancieraEntity> instituciones =
                institucionFinancieraRepository.findByEstadoRegistro(Constante.ESTADO_ACTIVO);

        return instituciones.stream()
                .filter(Objects::nonNull)
                .map(e -> InstitucionFinancieraDto.builder()
                        .codigo(e.getCodigo())
                        .codigoSbs(e.getCodigoSbs())
                        .build())
                .toList();
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

    private SolicitudSearchConnectDto getSolicitudSearchConnectDto(SolicitudSearchDto search) {
        SolicitudSearchConnectDto searchConnect = new SolicitudSearchConnectDto();
        List<String> users = usuarioRepository.findUsersForNames(search.getUsuario(), search.getCodigoCliente());
        searchConnect.setUsuarios(users);
        searchConnect.setFechaInicial(search.getFechaInicial());
        searchConnect.setFechaFinal(search.getFechaFinal());
        searchConnect.setCodigo(search.getCodigo());
        searchConnect.setEstadoSolicitud(search.getEstadoSolicitud());
        searchConnect.setUsuarioActual(search.getUsuarioActual());
        searchConnect.setProceso(search.getProceso());
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

    private String nvl(String value) {
        return value != null ? value : "";
    }

    private ConstanciaPagoResponse completarDatos(ConstanciaPagoResponse datos) {
        String nombreInstitucionOrigen = generalService.obtenerNombreInstitucionFinanciera(datos.getBancoOrigen());
        String nombreInstitucionDestino = generalService.obtenerNombreInstitucionFinanciera(datos.getEntidadDestino());

        datos.setBancoOrigen(nombreInstitucionOrigen);
        datos.setEntidadDestino(nombreInstitucionDestino);
        datos.setCuentaOrigen(enmascararTexto(datos.getCuentaOrigen()));

        return datos;
    }
}