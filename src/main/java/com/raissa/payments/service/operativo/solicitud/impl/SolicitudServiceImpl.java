package com.raissa.payments.service.operativo.solicitud.impl;

import com.raissa.comun.enums.commons.SortOrderEnum;
import com.raissa.comun.util.Constante;
import com.raissa.payments.commons.Constant;
import com.raissa.payments.commons.RestResponsePage;
import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingConnectRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.TrackingResponseDto;
import com.raissa.payments.exception.commons.ResponseApiException;
import com.raissa.payments.service.administrativo.general.ClienteDataSourceService;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class SolicitudServiceImpl extends AbstractRaissaPaymentsService implements SolicitudService {
    private final ClienteDataSourceService clienteDataSourceService;

    private final RestTemplate restTemplate;

    @Value("${conector.api.url}")
    private String urlConectorServer;

    public SolicitudSearchResponseDto getFindSolicitudesPage(SolicitudSearchDto search) {
        String sortField = validarSortField(search.getSortField(), Constant.VALID_PAIS_SORT_FIELDS, Constant.FIELD_CODIGO);
        Sort.Direction direction = SortOrderEnum.DESCENDENTE.getValor().equalsIgnoreCase(search.getSortOrder().getValor()) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(direction, sortField));//TODO pendiente hasta saber como mandar un pageable por la peticion

        SolicitudSearchConnectDto searchConnect = new SolicitudSearchConnectDto();
        searchConnect.setUsuario(search.getUsuario());
        searchConnect.setFechaInicial(search.getFechaInicial());
        searchConnect.setFechaFinal(search.getFechaFinal());
        searchConnect.setCodigo(search.getCodigo());
        searchConnect.setEstadoSolicitud(search.getEstadoSolicitud());

        String url = UriComponentsBuilder.fromUriString(urlConectorServer + "/flujo-solicitud/list-page-solicitud")
                .queryParam("page", search.getPage())
                .queryParam("size", search.getSize())
                .toUriString();

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(search.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SolicitudSearchConnectDto> requestEntity = new HttpEntity<>(searchConnect, headers);

        ResponseEntity<RestResponsePage<SolicitudResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API para obtener página de solicitudes");
        }

        List<SolicitudResponseDto> content = response.getBody().getContent()
                .stream()
                .toList();

        return new SolicitudSearchResponseDto(response.getBody().getTotalPages(),
                response.getBody().getTotalElements(),
                response.getBody().getNumber(),
                response.getBody().getSize(),
                content);
    }

    public Long flujoSolicitud(FlujoSolicitudRequestDto flujo, HttpServletRequest request){
        FlujoSolicitudConnectRequestDto flujoConnect = new FlujoSolicitudConnectRequestDto();
        flujoConnect.setSolicitudId(flujo.getIdSolicitud());
        flujoConnect.setUsuario(getCurrentUser());
        flujoConnect.setUsuarioAuditoria(getCurrentUser());
        flujoConnect.setFechaAuditoria(LocalDateTime.now());
        flujoConnect.setTerminalAuditoria(request.getRemoteHost());
        flujoConnect.setIpAuditoria(request.getRemoteAddr());


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
                new ParameterizedTypeReference<Long>() {}
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

        return response.getBody();
    }
}
