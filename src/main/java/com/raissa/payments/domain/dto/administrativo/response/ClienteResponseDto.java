package com.raissa.payments.domain.dto.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.payments.domain.dto.commons.TipoClienteResponseDto;
import lombok.Data;

@Data
public class ClienteResponseDto {
    private Long codigo;
    private String razonSocial;
    private String ruc;
    private TipoClienteResponseDto tipoCliente;
    private String direccion;
    private String telefonoFijo;
    private String telefonoCelular;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
