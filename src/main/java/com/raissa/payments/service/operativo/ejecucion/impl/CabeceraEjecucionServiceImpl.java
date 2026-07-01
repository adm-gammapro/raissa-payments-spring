package com.raissa.payments.service.operativo.ejecucion.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.commons.RestResponsePage;
import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.request.CabeceraEjecucionSearchConnectDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.request.CabeceraEjecucionSearchDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.CabeceraEjecucionResponseDto;
import com.raissa.payments.domain.dto.operativo.ejecucion.response.CabeceraEjecucionSearchResponseDto;
import com.raissa.payments.exception.commons.ResponseApiException;
import com.raissa.payments.service.administrativo.general.ClienteDataSourceService;
import com.raissa.payments.service.operativo.ejecucion.CabeceraEjecucionService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
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

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CabeceraEjecucionServiceImpl extends AbstractRaissaPaymentsService implements CabeceraEjecucionService {
    private final ClienteDataSourceService clienteDataSourceService;
    private final RestTemplate restTemplate;

    @Value("${conector.api.url}")
    private String urlConectorServer;

    @Override
    public CabeceraEjecucionResponseDto buscarPorId(Long id, Long codigoCliente) {
        String url = urlConectorServer + "/api/cabecera-ejecucion/" + id;

        HttpEntity<Void> request = buildRequest(null, codigoCliente);

        ResponseEntity<CabeceraEjecucionResponseDto> response =
                restTemplate.exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {
                        });

        validarRespuesta(response, "buscar cabecera-ejecucion id=" + id);
        return response.getBody();
    }

    @Override
    public CabeceraEjecucionSearchResponseDto buscarPaginado(CabeceraEjecucionSearchDto filtros) {

        CabeceraEjecucionSearchConnectDto connectDto =
                buildSearchConnectDto(filtros);

        String url = urlConectorServer + "/api/cabecera-ejecucion/buscar";

        HttpEntity<CabeceraEjecucionSearchConnectDto> request = buildRequest(connectDto, filtros.getCodigoCliente());

        ResponseEntity<RestResponsePage<CabeceraEjecucionResponseDto>> response =
                restTemplate.exchange(url, HttpMethod.POST, request,
                        new ParameterizedTypeReference<>() {
                        });

        validarRespuesta(response, "buscar paginado cabecera-ejecucion");

        RestResponsePage<CabeceraEjecucionResponseDto> page = response.getBody();
        return new CabeceraEjecucionSearchResponseDto(page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getContent());
    }

    private CabeceraEjecucionSearchConnectDto buildSearchConnectDto(CabeceraEjecucionSearchDto src) {
        CabeceraEjecucionSearchConnectDto dto = new CabeceraEjecucionSearchConnectDto();
        dto.setEstadoProcesamiento(src.getEstadoProcesamiento());
        dto.setFechaInicial(src.getFechaInicial());
        dto.setFechaFinal(src.getFechaFinal());
        dto.setPage(src.getPage());
        dto.setSize(src.getSize());
        dto.setSortField(src.getSortField());
        dto.setSortOrder(src.getSortOrder());
        return dto;
    }

    private <T> HttpEntity<T> buildRequest(T body, Long codigoCliente) {
        ClienteDataSourceResponseDto dataSource =
                clienteDataSourceService.getDataSource(codigoCliente);
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, dataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private <T> void validarRespuesta(ResponseEntity<T> response, String operacion) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseApiException("Error al llamar al API del conector: " + operacion);
        }
    }
}
