package com.raissa.payments.service.operativo.administrativo.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.commons.RestResponsePage;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CuentaOrdenanteConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CuentaOrdenanteResponseDto;
import com.raissa.payments.service.administrativo.general.ClienteDataSourceService;
import com.raissa.payments.service.operativo.administrativo.CuentaOrdenanteService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CuentaOrdenanteServiceImpl extends AbstractRaissaPaymentsService implements CuentaOrdenanteService {
    private final ClienteDataSourceService clienteDataSourceService;
    private final RestTemplate restTemplate;

    @Value("${conector.api.url}")
    private String urlConectorServer;

    @Override
    public CuentaOrdenanteConnectResponseDto listarCuentaOrdenantePage(CuentaOrdenanteSearchDto search) {
        log.info("Listando cuenta ordenante page - Cliente: {}, NumeroCuenta: {}",
                search.getCodigoCliente(), search.getNumeroCuentaOrdenante());

        CuentaOrdenanteSearchConnectDto searchConnect = new CuentaOrdenanteSearchConnectDto();
        searchConnect.setNumeroCuentaOrdenante(search.getNumeroCuentaOrdenante());
        searchConnect.setEstadoRegistro(search.getEstadoRegistro());
        searchConnect.setSize(search.getSize());
        searchConnect.setSortField(search.getSortField());
        searchConnect.setSortOrder(search.getSortOrder());
        searchConnect.setPage(search.getPage());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/cuenta-ordenante/list-page")
                .toUriString();

        var clienteDataSource = clienteDataSourceService.getDataSource(search.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CuentaOrdenanteSearchConnectDto> requestEntity = new HttpEntity<>(searchConnect, headers);

        ResponseEntity<RestResponsePage<CuentaOrdenanteResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Error al llamar al API para obtener página de cuentas ordenantes");
        }

        List<CuentaOrdenanteResponseDto> content = response.getBody().getContent();

        return new CuentaOrdenanteConnectResponseDto(
                response.getBody().getTotalPages(),
                response.getBody().getTotalElements(),
                response.getBody().getNumber(),
                response.getBody().getSize(),
                content
        );
    }

    @Override
    public CuentaOrdenanteResponseDto getCuentaOrdenante(CuentaOrdenanteRequestDto get) {
        log.info("Obteniendo cuenta ordenante - Codigo: {}, Cliente: {}", get.getCodigo(), get.getCodigoCliente());

        CuentaOrdenanteConnectRequestDto requestConnect = new CuentaOrdenanteConnectRequestDto();
        requestConnect.setCodigo(get.getCodigo());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/cuenta-ordenante/get")
                .toUriString();

        var clienteDataSource = clienteDataSourceService.getDataSource(get.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CuentaOrdenanteConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CuentaOrdenanteResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Error al llamar al API para obtener cuenta ordenante");
        }

        return response.getBody();
    }

    @Override
    public CuentaOrdenanteResponseDto createCuentaOrdenante(CuentaOrdenanteRequestDto create, HttpServletRequest request) {
        log.info("Creando cuenta ordenante - Cliente: {}, Usuario: {}",
                create.getCodigoCliente(), create.getUsuarioOrdenante());

        CuentaOrdenanteConnectRequestDto requestConnect = new CuentaOrdenanteConnectRequestDto();
        requestConnect.setUsuarioOrdenante(create.getUsuarioOrdenante());
        requestConnect.setPasswordOrdenante(create.getPasswordOrdenante());
        requestConnect.setNumeroCuentaOrdenante(create.getNumeroCuentaOrdenante());
        requestConnect.setMonedaCuentaOrdenante(create.getMonedaCuentaOrdenante());
        requestConnect.setTipoDocumentoOrdenante(create.getTipoDocumentoOrdenante());
        requestConnect.setDocumentoOrdenante(create.getDocumentoOrdenante());
        requestConnect.setNombreOrdenante(create.getNombreOrdenante());
        requestConnect.setApellidoPaternoOrdenante(create.getApellidoPaternoOrdenante());
        requestConnect.setApellidoMaternoOrdenante(create.getApellidoMaternoOrdenante());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/cuenta-ordenante/create")
                .toUriString();

        var clienteDataSource = clienteDataSourceService.getDataSource(create.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CuentaOrdenanteConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CuentaOrdenanteResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Error al llamar al API para registrar cuenta ordenante");
        }

        log.info("Cuenta ordenante creada exitosamente - ID: {}", response.getBody().getCodigo());
        return response.getBody();
    }

    @Override
    public CuentaOrdenanteResponseDto updateCuentaOrdenante(CuentaOrdenanteRequestDto update, HttpServletRequest request) {
        log.info("Actualizando cuenta ordenante - Codigo: {}, Cliente: {}",
                update.getCodigo(), update.getCodigoCliente());

        CuentaOrdenanteConnectRequestDto requestConnect = new CuentaOrdenanteConnectRequestDto();
        requestConnect.setCodigo(update.getCodigo());
        requestConnect.setUsuarioOrdenante(update.getUsuarioOrdenante());
        requestConnect.setPasswordOrdenante(update.getPasswordOrdenante());
        requestConnect.setNumeroCuentaOrdenante(update.getNumeroCuentaOrdenante());
        requestConnect.setMonedaCuentaOrdenante(update.getMonedaCuentaOrdenante());
        requestConnect.setTipoDocumentoOrdenante(update.getTipoDocumentoOrdenante());
        requestConnect.setDocumentoOrdenante(update.getDocumentoOrdenante());
        requestConnect.setNombreOrdenante(update.getNombreOrdenante());
        requestConnect.setApellidoPaternoOrdenante(update.getApellidoPaternoOrdenante());
        requestConnect.setApellidoMaternoOrdenante(update.getApellidoMaternoOrdenante());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/cuenta-ordenante/update")
                .toUriString();

        var clienteDataSource = clienteDataSourceService.getDataSource(update.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CuentaOrdenanteConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CuentaOrdenanteResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Error al llamar al API para actualizar cuenta ordenante");
        }

        log.info("Cuenta ordenante actualizada exitosamente - ID: {}", response.getBody().getCodigo());
        return response.getBody();
    }

    @Override
    public CuentaOrdenanteResponseDto deleteCuentaOrdenante(CuentaOrdenanteRequestDto delete, HttpServletRequest request) {
        log.info("Eliminando cuenta ordenante - Codigo: {}, Cliente: {}",
                delete.getCodigo(), delete.getCodigoCliente());

        CuentaOrdenanteConnectRequestDto requestConnect = new CuentaOrdenanteConnectRequestDto();
        requestConnect.setCodigo(delete.getCodigo());
        requestConnect.setUsuarioAuditoria(getCurrentUser());
        requestConnect.setFechaAuditoria(LocalDateTime.now());
        requestConnect.setTerminalAuditoria(request.getRemoteHost());
        requestConnect.setIpAuditoria(request.getRemoteAddr());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/cuenta-ordenante/delete")
                .toUriString();

        var clienteDataSource = clienteDataSourceService.getDataSource(delete.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CuentaOrdenanteConnectRequestDto> requestEntity = new HttpEntity<>(requestConnect, headers);

        ResponseEntity<CuentaOrdenanteResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Error al llamar al API para eliminar cuenta ordenante");
        }

        log.info("Cuenta ordenante eliminada exitosamente - ID: {}", response.getBody().getCodigo());
        return response.getBody();
    }

    @Override
    public List<CuentaOrdenanteResponseDto> listCuentaOrdenante(CuentaOrdenanteRequestDto list) {
        log.info("Listando cuentas ordenantes - Cliente: {}", list.getCodigoCliente());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/cuenta-ordenante/list")
                .toUriString();

        var clienteDataSource = clienteDataSourceService.getDataSource(list.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<CuentaOrdenanteResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Error al llamar al API para listar cuentas ordenantes");
        }

        return response.getBody();
    }
}
