package com.raissa.payments.service.operativo.administrativo.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.commons.RestResponsePage;
import com.raissa.payments.domain.dto.administrativo.response.UsuarioResponseDto;
import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaUsuarioConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaUsuarioRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConfiguracionReglaConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConfiguracionReglaRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConfiguracionReglaSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConfiguracionReglaSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ReglaConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ReglaRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ReglaSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ReglaSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.TipoPagoConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.TipoPagoRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.TipoPagoSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.TipoPagoSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.VinculoCategoriaUsuarioConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.VinculoCategoriaUsuarioRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CategoriaConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CategoriaResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ConfiguracionReglaConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ConfiguracionReglaResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ReglaConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ReglaResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.TipoPagoConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.TipoPagoResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.VinculoCategoriaUsuarioConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.VinculoCategoriaUsuarioResponseDto;
import com.raissa.payments.domain.entity.administrativo.UsuarioEntity;
import com.raissa.payments.domain.mappers.administrativo.UsuarioMapper;
import com.raissa.payments.domain.repository.administrativo.UsuarioRepository;
import com.raissa.payments.exception.commons.BusinessException;
import com.raissa.payments.exception.commons.ResponseApiException;
import com.raissa.payments.service.administrativo.general.ClienteDataSourceService;
import com.raissa.payments.service.operativo.administrativo.AdministrativoService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministrativoServiceImpl extends AbstractRaissaPaymentsService implements AdministrativoService {
    private final ClienteDataSourceService clienteDataSourceService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    private final RestTemplate restTemplate;

    @Value("${conector.api.url}")
    private String urlConectorServer;

    public TipoPagoConnectResponseDto listarTipoPagoPage(TipoPagoSearchDto search) {
        TipoPagoSearchConnectDto searchConnect = new TipoPagoSearchConnectDto();
        searchConnect.setDescripcion(search.getDescripcion());
        searchConnect.setEstadoRegistro(search.getEstadoRegistro());
        searchConnect.setSize(search.getSize());
        searchConnect.setSortField(search.getSortField());
        searchConnect.setSortOrder(search.getSortOrder());
        searchConnect.setPage(search.getPage());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/tipo-pago/list-page-tipo-pago").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(search.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TipoPagoSearchConnectDto> requestEntity = new HttpEntity<>(searchConnect, headers);

        ResponseEntity<RestResponsePage<TipoPagoResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de tipos de pago");
        }

        List<TipoPagoResponseDto> content = response.getBody().getContent()
                .stream()
                .toList();

        return new TipoPagoConnectResponseDto(response.getBody().getTotalPages(),
                response.getBody().getTotalElements(),
                response.getBody().getNumber(),
                response.getBody().getSize(),
                content);
    }

    public TipoPagoResponseDto getTipopago(TipoPagoRequestDto get) {
        TipoPagoConnectRequestDto requestConnect = new TipoPagoConnectRequestDto();
        requestConnect.setCodigo(get.getCodigo());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/tipo-pago/get-tipo-pago").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TipoPagoConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<TipoPagoResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener un tipo de pago");
        }

        return response.getBody();
    }

    public TipoPagoResponseDto createTipoPago(TipoPagoRequestDto create,
                                              HttpServletRequest request) {
        TipoPagoConnectRequestDto requestConnect = new TipoPagoConnectRequestDto();
        requestConnect.setDescripcion(create.getDescripcion());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/tipo-pago/create-tipo-pago").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(create.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TipoPagoConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<TipoPagoResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para registrar tipo de pago");
        }

        return response.getBody();
    }

    public TipoPagoResponseDto updateTipoPago(TipoPagoRequestDto update,
                                              HttpServletRequest request) {
        TipoPagoConnectRequestDto requestConnect = new TipoPagoConnectRequestDto();
        requestConnect.setCodigo(update.getCodigo());
        requestConnect.setDescripcion(update.getDescripcion());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/tipo-pago/update-tipo-pago").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(update.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TipoPagoConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<TipoPagoResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para actualizar tipo de pago");
        }

        return response.getBody();
    }

    public TipoPagoResponseDto deleteTipoPago(TipoPagoRequestDto delete,
                                              HttpServletRequest request) {
        TipoPagoConnectRequestDto requestConnect = new TipoPagoConnectRequestDto();
        requestConnect.setCodigo(delete.getCodigo());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/tipo-pago/delete-tipo-pago").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(delete.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TipoPagoConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<TipoPagoResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener eliminar tipo de pago");
        }

        return response.getBody();
    }

    public List<TipoPagoResponseDto> listTipopago(TipoPagoRequestDto get) {
        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/tipo-pago/get-tipo-pago").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TipoPagoConnectRequestDto> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<TipoPagoResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener un tipo de pago");
        }

        return response.getBody();
    }

    public CategoriaConnectResponseDto listarCategoriaPage(CategoriaSearchDto search) {
        CategoriaSearchConnectDto searchConnect = new CategoriaSearchConnectDto();
        searchConnect.setDescripcion(search.getDescripcion());
        searchConnect.setEstadoRegistro(search.getEstadoRegistro());
        searchConnect.setSize(search.getSize());
        searchConnect.setSortField(search.getSortField());
        searchConnect.setSortOrder(search.getSortOrder());
        searchConnect.setPage(search.getPage());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/list-page-categoria").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(search.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaSearchConnectDto> requestEntity = new HttpEntity<>(searchConnect, headers);

        ResponseEntity<RestResponsePage<CategoriaResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de categorias");
        }

        List<CategoriaResponseDto> content = response.getBody().getContent()
                .stream()
                .toList();

        return new CategoriaConnectResponseDto(response.getBody().getTotalPages(),
                response.getBody().getTotalElements(),
                response.getBody().getNumber(),
                response.getBody().getSize(),
                content);
    }

    public CategoriaResponseDto getCategoria(CategoriaRequestDto get) {
        CategoriaConnectRequestDto requestConnect = new CategoriaConnectRequestDto();
        requestConnect.setCodigo(get.getCodigo());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/get-categoria").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CategoriaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener una categoria");
        }

        return response.getBody();
    }

    public CategoriaResponseDto createCategoria(CategoriaRequestDto create,
                                                HttpServletRequest request) {
        CategoriaConnectRequestDto requestConnect = new CategoriaConnectRequestDto();
        requestConnect.setDescripcion(create.getDescripcion());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/create-categoria").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(create.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CategoriaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para crear una categoria");
        }

        return response.getBody();
    }

    public CategoriaResponseDto updateCategoria(CategoriaRequestDto update,
                                                HttpServletRequest request) {
        CategoriaConnectRequestDto requestConnect = new CategoriaConnectRequestDto();
        requestConnect.setCodigo(update.getCodigo());
        requestConnect.setDescripcion(update.getDescripcion());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/update-categoria").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(update.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CategoriaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para actualizar categoria");
        }

        return response.getBody();
    }

    public CategoriaResponseDto deleteCategoria(CategoriaRequestDto delete,
                                                HttpServletRequest request) {
        CategoriaConnectRequestDto requestConnect = new CategoriaConnectRequestDto();
        requestConnect.setCodigo(delete.getCodigo());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/delete-categoria").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(delete.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CategoriaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para eliminar categoria");
        }

        return response.getBody();
    }

    public List<CategoriaResponseDto> listCategoria(CategoriaRequestDto get) {
        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/list-categoria").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaConnectRequestDto> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CategoriaResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener una categoria");
        }

        return response.getBody();
    }

    public VinculoCategoriaUsuarioResponseDto listVinculoCategoriaUsuario(VinculoCategoriaUsuarioRequestDto req) {
        VinculoCategoriaUsuarioResponseDto response = new VinculoCategoriaUsuarioResponseDto();
        VinculoCategoriaUsuarioConnectResponseDto vinculo = obtenerVinculoCategoriaUsuario(req);

        if(vinculo != null) {
            response.setIdCategoria(vinculo.getIdCategoria());
        } else {
            throw new BusinessException("ID Categoria no disponible");
        }

        List<UsuarioEntity> usuariosVinculados = usuarioRepository.findByUsernameInAndEstadoRegistro(vinculo.getUsuariosVinculados(),
                Constante.ESTADO_ACTIVO);

        List<UsuarioResponseDto> usuariosVinculadosDto =
                usuariosVinculados.stream()
                        .map(usuarioMapper::entityToResponseDto)
                        .toList();

        List<UsuarioEntity> usuariosDisponibles =  usuarioRepository.findByUsuariosDisponibles(vinculo.getUsuariosVinculados(),
                req.getCodigoCliente(),
                Constante.ESTADO_ACTIVO);

        List<UsuarioResponseDto> usuariosDisponiblesDto =
                usuariosDisponibles.stream()
                        .map(usuarioMapper::entityToResponseDto)
                        .toList();

        response.setUsuariosVinculados(usuariosVinculadosDto);
        response.setUsuariosDisponibles(usuariosDisponiblesDto);

        return response;
    }

    public Boolean vincularCategoriaUsuario(CategoriaUsuarioRequestDto req,
                                            HttpServletRequest request) {
        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/vincular-categoria-usuario").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(req.getCodigoCliente());

        CategoriaUsuarioConnectRequestDto connect = new CategoriaUsuarioConnectRequestDto();
        connect.setIdCategoria(req.getIdCategoria());
        connect.setUsernames(req.getUsernames());
        connect.setUsuarioAuditoria(getCurrentUser());
        connect.setFechaAuditoria(LocalDateTime.now());
        connect.setTerminalAuditoria(request.getRemoteHost());
        connect.setIpAuditoria(request.getRemoteAddr());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaUsuarioConnectRequestDto> requestEntity = new HttpEntity<>(connect, headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para vincular categoria usuario");
        }

        return response.getBody();
    }

    public Boolean desvincularCategoriaUsuario(CategoriaUsuarioRequestDto req,
                                               HttpServletRequest request) {
        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/desvincular-categoria-usuario").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(req.getCodigoCliente());

        CategoriaUsuarioConnectRequestDto connect = new CategoriaUsuarioConnectRequestDto();
        connect.setIdCategoria(req.getIdCategoria());
        connect.setUsernames(req.getUsernames());
        connect.setUsuarioAuditoria(getCurrentUser());
        connect.setFechaAuditoria(LocalDateTime.now());
        connect.setTerminalAuditoria(request.getRemoteHost());
        connect.setIpAuditoria(request.getRemoteAddr());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CategoriaUsuarioConnectRequestDto> requestEntity = new HttpEntity<>(connect, headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para vincular categoria usuario");
        }

        return response.getBody();
    }

    public ReglaConnectResponseDto listarReglaPage(ReglaSearchDto search) {
        ReglaSearchConnectDto searchConnect = new ReglaSearchConnectDto();
        searchConnect.setDescripcion(search.getDescripcion());
        searchConnect.setMoneda(search.getMoneda());
        searchConnect.setEstadoRegistro(search.getEstadoRegistro());
        searchConnect.setSize(search.getSize());
        searchConnect.setSortField(search.getSortField());
        searchConnect.setSortOrder(search.getSortOrder());
        searchConnect.setPage(search.getPage());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/regla/list-page-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(search.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReglaSearchConnectDto> requestEntity = new HttpEntity<>(searchConnect, headers);

        ResponseEntity<RestResponsePage<ReglaResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        RestResponsePage<ReglaResponseDto> body = response.getBody();
        List<ReglaResponseDto> content = body.getContent().stream().toList();

        return new ReglaConnectResponseDto(response.getBody().getTotalPages(),
                response.getBody().getTotalElements(),
                response.getBody().getNumber(),
                response.getBody().getSize(),
                content);
    }

    public ReglaResponseDto getRegla(ReglaRequestDto get) {
        ReglaConnectRequestDto requestConnect = new ReglaConnectRequestDto();
        requestConnect.setCodigo(get.getCodigo());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/regla/get-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener una regla");
        }

        return response.getBody();
    }

    public ReglaResponseDto createRegla(ReglaRequestDto create,
                                        HttpServletRequest request) {
        ReglaConnectRequestDto requestConnect = new ReglaConnectRequestDto();
        requestConnect.setDescripcion(create.getDescripcion());
        requestConnect.setMoneda(create.getMoneda());
        requestConnect.setLimiteInferior(create.getLimiteInferior());
        requestConnect.setLimiteSuperior(create.getLimiteSuperior());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/regla/create-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(create.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para crear una regla");
        }

        return response.getBody();
    }

    public ReglaResponseDto updateRegla(ReglaRequestDto update,
                                        HttpServletRequest request) {
        ReglaConnectRequestDto requestConnect = new ReglaConnectRequestDto();
        requestConnect.setCodigo(update.getCodigo());
        requestConnect.setDescripcion(update.getDescripcion());
        requestConnect.setMoneda(update.getMoneda());
        requestConnect.setLimiteInferior(update.getLimiteInferior());
        requestConnect.setLimiteSuperior(update.getLimiteSuperior());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/regla/update-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(update.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para actualizar una regla");
        }

        return response.getBody();
    }

    public ReglaResponseDto deleteRegla(ReglaRequestDto delete,
                                        HttpServletRequest request) {
        ReglaConnectRequestDto requestConnect = new ReglaConnectRequestDto();
        requestConnect.setCodigo(delete.getCodigo());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/regla/delete-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(delete.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para eliminar regla");
        }

        return response.getBody();
    }

    public List<ReglaResponseDto> listRegla(ReglaRequestDto get) {
        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/regla/list-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReglaConnectRequestDto> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<ReglaResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener una regla");
        }

        return response.getBody();
    }

    public ConfiguracionReglaConnectResponseDto listarConfiguracionReglaPage(ConfiguracionReglaSearchDto search) {
        ConfiguracionReglaSearchConnectDto searchConnect = new ConfiguracionReglaSearchConnectDto();
        searchConnect.setCodigoRegla(search.getCodigoRegla());
        searchConnect.setCodigoCategoria(search.getCodigoCategoria());
        searchConnect.setCodigoModo(search.getCodigoModo());
        searchConnect.setEstadoRegistro(search.getEstadoRegistro());
        searchConnect.setSize(search.getSize());
        searchConnect.setSortField(search.getSortField());
        searchConnect.setSortOrder(search.getSortOrder());
        searchConnect.setPage(search.getPage());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/configuracion-regla/list-page-configuracion-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(search.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConfiguracionReglaSearchConnectDto> requestEntity = new HttpEntity<>(searchConnect, headers);

        ResponseEntity<RestResponsePage<ConfiguracionReglaResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de configuración regla");
        }

        RestResponsePage<ConfiguracionReglaResponseDto> body = response.getBody();
        List<ConfiguracionReglaResponseDto> content = body.getContent().stream().toList();

        return new ConfiguracionReglaConnectResponseDto(response.getBody().getTotalPages(),
                response.getBody().getTotalElements(),
                response.getBody().getNumber(),
                response.getBody().getSize(),
                content);
    }

    public ConfiguracionReglaResponseDto getConfiguracionRegla(ConfiguracionReglaRequestDto get) {
        ConfiguracionReglaConnectRequestDto requestConnect = new ConfiguracionReglaConnectRequestDto();
        requestConnect.setCodigo(get.getCodigo());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/configuracion-regla/get-configuracion-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConfiguracionReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ConfiguracionReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener una configuración regla");
        }

        return response.getBody();
    }

    public ConfiguracionReglaResponseDto createConfiguracionRegla(ConfiguracionReglaRequestDto create,
                                                                  HttpServletRequest request) {
        ConfiguracionReglaConnectRequestDto requestConnect = new ConfiguracionReglaConnectRequestDto();
        requestConnect.setCodigoRegla(create.getCodigoRegla());
        requestConnect.setCodigoCategoria(create.getCodigoCategoria());
        requestConnect.setCodigoModo(create.getCodigoModo());
        requestConnect.setPredeterminado(create.getPredeterminado());
        requestConnect.setPrioridad(create.getPrioridad());
        // Agrega otros campos específicos de tu configuración regla
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/configuracion-regla/create-configuracion-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(create.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConfiguracionReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ConfiguracionReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para crear una configuración regla");
        }

        return response.getBody();
    }

    public ConfiguracionReglaResponseDto updateConfiguracionRegla(ConfiguracionReglaRequestDto update,
                                                                  HttpServletRequest request) {
        ConfiguracionReglaConnectRequestDto requestConnect = new ConfiguracionReglaConnectRequestDto();
        requestConnect.setCodigo(update.getCodigo());
        requestConnect.setCodigoRegla(update.getCodigoRegla());
        requestConnect.setCodigoCategoria(update.getCodigoCategoria());
        requestConnect.setCodigoModo(update.getCodigoModo());
        requestConnect.setPredeterminado(update.getPredeterminado());
        requestConnect.setPrioridad(update.getPrioridad());
        // Agrega otros campos específicos de tu configuración regla
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/configuracion-regla/update-configuracion-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(update.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConfiguracionReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ConfiguracionReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para actualizar una configuración regla");
        }

        return response.getBody();
    }

    public ConfiguracionReglaResponseDto deleteConfiguracionRegla(ConfiguracionReglaRequestDto delete,
                                                                  HttpServletRequest request) {
        ConfiguracionReglaConnectRequestDto requestConnect = new ConfiguracionReglaConnectRequestDto();
        requestConnect.setCodigo(delete.getCodigo());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/configuracion-regla/delete-configuracion-regla").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(delete.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ConfiguracionReglaConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<ConfiguracionReglaResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para eliminar configuración regla");
        }

        return response.getBody();
    }

    private VinculoCategoriaUsuarioConnectResponseDto obtenerVinculoCategoriaUsuario(VinculoCategoriaUsuarioRequestDto req) {
        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/categorias/list-vinculo-categoria-usuario").toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(req.getCodigoCliente());

        VinculoCategoriaUsuarioConnectRequestDto connect = new VinculoCategoriaUsuarioConnectRequestDto();
        connect.setIdCategoria(req.getIdCategoria());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VinculoCategoriaUsuarioConnectRequestDto> requestEntity = new HttpEntity<>(connect, headers);

        ResponseEntity<VinculoCategoriaUsuarioConnectResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener una categoria");
        }

        return response.getBody();
    }
}