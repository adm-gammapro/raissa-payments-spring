package com.raissa.payments.service.operativo.solicitud.impl;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.CargaSolicitudJsonRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.CargaSolicitudResponseDto;
import com.raissa.payments.exception.commons.ResponseApiException;
import com.raissa.payments.service.administrativo.general.ClienteDataSourceService;
import com.raissa.payments.service.operativo.solicitud.CargarSolicitudConectorService;
import com.raissa.payments.util.AbstractRaissaPaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CargarSolicitudConectorServiceImpl extends AbstractRaissaPaymentsService implements CargarSolicitudConectorService {
    private final ClienteDataSourceService clienteDataSourceService;

    //Templates
    private final RestTemplate restTemplate;

    @Value("${conector.api.url}")
    private String urlConectorServer;

    public CargaSolicitudResponseDto createSolicitud(CargaSolicitudJsonRequestDto requestDto, HttpServletRequest request) {
        String url = urlConectorServer + "/solicitudes/cargar-json";

        ClienteDataSourceResponseDto clienteDataSource = clienteDataSourceService.getDataSource(requestDto.getCodigoCliente());

        HttpHeaders headers = new HttpHeaders();
        headers.set(Constante.PARAM_HEADER_EMPRESA, clienteDataSource.getCodigoDataSource());
        headers.setContentType(MediaType.APPLICATION_JSON);

        requestDto.setUsuarioAuditoria(getCurrentUser());
        requestDto.setFechaAuditoria(LocalDateTime.now());
        requestDto.setTerminalAuditoria(request.getRemoteHost());
        requestDto.setIpAuditoria(request.getRemoteAddr());

        HttpEntity<CargaSolicitudJsonRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<CargaSolicitudResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                CargaSolicitudResponseDto.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new ResponseApiException("Error al llamar al API para crear una solicitud");
        }
    }
}