package com.raissa.payments.rest.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudRest {
    private final SolicitudService solicitudService;

    @PostMapping("/list-page-solicitud")
    public ResponseEntity<SolicitudSearchResponseDto> listarCuentaPage(@RequestBody SolicitudSearchDto search) {
        return ResponseEntity.ok(solicitudService.getFindSolicitudesPage(search));
    }
}
