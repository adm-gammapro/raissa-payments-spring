package com.raissa.payments.rest.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.administrativo.request.ConsultaVoucherAbonoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ConstanciaPagoResponse;
import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.LiquidacionSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionFlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.LiquidacionSolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping("/constancia-pago")
    public ResponseEntity<ConstanciaPagoResponse> obtenerConstanciaPago(@Valid @RequestBody ConsultaVoucherAbonoSolicitudRequestDto req) {
        ConstanciaPagoResponse response = solicitudService.obtenerConstanciaPago(req);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/constancia-pago/pdf")
    public ResponseEntity<byte[]> descargarConstanciaPagoPDF(@Valid @RequestBody ConsultaVoucherAbonoSolicitudRequestDto req) {
        byte[] pdfBytes = solicitudService.generarConstanciaPagoPDF(req);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "constancia-pago.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/constancia-pago/enviar-correo")
    public ResponseEntity<Void> enviarConstanciaPagoCorreo(@Valid @RequestBody ConsultaVoucherAbonoSolicitudRequestDto req) {
        solicitudService.enviarConstanciaPagoCorreo(req);
        return ResponseEntity.ok().build();
    }
}
