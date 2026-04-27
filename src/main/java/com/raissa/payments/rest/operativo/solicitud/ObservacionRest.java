package com.raissa.payments.rest.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.ObservacionResponseDto;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/observacion")
@RequiredArgsConstructor
public class ObservacionRest {
    private final SolicitudService solicitudService;

    @PostMapping("/list-observacion")
    public ResponseEntity<List<ObservacionResponseDto>> listarObservaciones(@RequestBody ObservacionRequestDto search) {
        return ResponseEntity.ok(solicitudService.listObservacion(search));
    }
}
