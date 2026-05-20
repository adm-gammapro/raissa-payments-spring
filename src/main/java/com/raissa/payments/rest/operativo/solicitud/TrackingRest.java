package com.raissa.payments.rest.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.TrackingResponseDto;
import com.raissa.payments.service.operativo.solicitud.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Tracking",
        description = "Servicios relacionados al seguimiento y trazabilidad de solicitudes"
)
public class TrackingRest {
    private final SolicitudService solicitudService;

    @Operation(
            summary = "Listar tracking de solicitud",
            description = """
                    Obtiene el historial de seguimiento (tracking)
                    asociado a una solicitud registrada en el sistema.
                    """
    )
    @PostMapping("/list-tracking")
    public ResponseEntity<List<TrackingResponseDto>> listarTracking(@RequestBody TrackingRequestDto search) {
        return ResponseEntity.ok(solicitudService.listTracking(search));
    }
}
