package com.raissa.payments.domain.dto.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class PerfilResponseDto {
    private Long codigo;
    private String descripcion;
    private String abreviatura;
    private String nombreComercial;
    private String fechaCaducidad;
    private String idUsuario;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
