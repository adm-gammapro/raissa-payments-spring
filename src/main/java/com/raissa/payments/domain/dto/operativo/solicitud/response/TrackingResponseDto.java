package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Información del tracking de la solicitud")
public class TrackingResponseDto {
    @Schema(
            description = "Fecha del evento",
            example = "07/05/2026"
    )
    private String fecha;

    @Schema(
            description = "Usuario que realizó la acción",
            example = "DNI_00000000"
    )
    private String usuario;

    @Schema(
            description = "Nombre completo del usuario",
            example = "Juan Pérez"
    )
    private String nombreUsuario;

    @Schema(
            description = "Evento realizado",
            example = "VALIDAR"
    )
    private String evento;

    @Schema(
            description = "Estado del registro",
            example = "VIGENTE"
    )
    private EstadoRegistroEnum estadoRegistro;

    @Schema(
            description = "Fecha de registro de la tupla",
            example = "2026-05-07 10:30:00"
    )
    private String audiFechIns;
}
