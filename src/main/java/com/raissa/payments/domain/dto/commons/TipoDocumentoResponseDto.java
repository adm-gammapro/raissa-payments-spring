package com.raissa.payments.domain.dto.commons;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class TipoDocumentoResponseDto {
    private String codigo;
    private String abreviatura;
    private String descripcion;
    private Integer longitudMinima;
    private Integer longitudMaxima;
    private String indicadorPersonaJuridica;
    private String indicadorPersonaNatural;
    private String documentoComplementario;
    private String equivalenteSbs;
    private String equivalenteSentinel;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
