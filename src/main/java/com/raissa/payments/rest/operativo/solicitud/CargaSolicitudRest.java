package com.raissa.payments.rest.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.CargaSolicitudJsonRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.CargaSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.CargaSolicitudResponseDto;
import com.raissa.payments.service.operativo.solicitud.CargaSolicitudService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/carga-solicitudes")
@RequiredArgsConstructor
public class CargaSolicitudRest {
    private final CargaSolicitudService service;

    @PostMapping(value = "/cargar-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CargaSolicitudResponseDto> cargar(@RequestPart("file") MultipartFile file,
                                                            @RequestParam("idEmpresa") Long idEmpresa,
                                                            @RequestParam("usuarioCarga") String usuarioCarga,
                                                            HttpServletRequest request) {
        CargaSolicitudRequestDto req = new CargaSolicitudRequestDto(idEmpresa, usuarioCarga, file);
        return ResponseEntity.ok(service.cargar(req, request));
    }

    @PostMapping(value = "/cargar-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CargaSolicitudResponseDto> cargarJson(@RequestBody CargaSolicitudJsonRequestDto req,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(service.cargarDesdeJson(req, request));
    }
}