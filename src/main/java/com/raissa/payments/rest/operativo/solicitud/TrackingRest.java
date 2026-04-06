package com.raissa.payments.rest.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.TrackingResponseDto;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingRest {
    private final SolicitudService solicitudService;

    @PostMapping("/list-tracking")
    public ResponseEntity<List<TrackingResponseDto>> listarSolicitudesPage(@RequestBody TrackingRequestDto search) {
        return ResponseEntity.ok(solicitudService.listTracking(search));
    }
}
