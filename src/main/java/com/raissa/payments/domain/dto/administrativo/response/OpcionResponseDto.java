package com.raissa.payments.domain.dto.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpcionResponseDto {
    private Long codigo;
    private String descripcionOpcion;
    private String rutaOpcion;
    private String parteFija;
    private String icono;
    private Long opcionPadre;
    private String descripcionOpcionPadre;
    private Integer numeroOrden;
    private Long codigoModulo;
    private String descripcionModulo;
    private String seleccionable;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
