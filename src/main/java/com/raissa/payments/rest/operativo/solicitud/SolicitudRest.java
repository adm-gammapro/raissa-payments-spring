package com.raissa.payments.rest.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.LiquidacionSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionFlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.LiquidacionSolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<SolicitudSearchResponseDto> listarSolicitudesPage(@RequestBody SolicitudSearchDto search) {
        return ResponseEntity.ok(solicitudService.getFindSolicitudesPage(search));
    }

    @PostMapping("/flujo-solicitud")
    public ResponseEntity<Long> validarSolicitudes(@RequestBody FlujoSolicitudRequestDto flujo,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(solicitudService.flujoSolicitud(flujo, request));
    }

    @PostMapping("/flujo-solicitud-observacion")
    public ResponseEntity<Long> observarSolicitudes(@RequestBody ObservacionFlujoSolicitudRequestDto flujo,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(solicitudService.flujoSolicitudObservacion(flujo, request));
    }

    @PostMapping("/tracking")
    public ResponseEntity<Long> trackingSolicitudes(@RequestBody FlujoSolicitudRequestDto flujo,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(solicitudService.flujoSolicitud(flujo, request));
    }

    @PostMapping("/resumen-liquidacion")
    public ResponseEntity<LiquidacionSolicitudResponseDto> gerResumenLiquidacion(@RequestBody LiquidacionSolicitudRequestDto req) {
        return ResponseEntity.ok(solicitudService.getResumenLiquidacion(req));
    }
}
