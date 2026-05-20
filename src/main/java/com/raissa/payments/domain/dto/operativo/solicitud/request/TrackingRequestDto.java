package com.raissa.payments.domain.dto.operativo.solicitud.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Parámetros de búsqueda del tracking")
public class TrackingRequestDto {
    @Schema(
            description = "Identificador de la solicitud",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idSolicitud;

    @Schema(
            description = "Código del cliente",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long codigoCliente;
}