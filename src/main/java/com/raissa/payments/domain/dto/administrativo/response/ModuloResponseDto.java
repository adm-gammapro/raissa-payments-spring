package com.raissa.payments.domain.dto.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModuloResponseDto {
    private Long codigo;
    private String nombreModulo;
    private String descripcion;
    private String subtitulo;
    private String icono;
    private String codigoAplicacion;
    private String descripcionAplicacion;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
