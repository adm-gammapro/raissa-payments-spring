package com.raissa.payments.domain.dto.commons;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.payments.domain.dto.administrativo.response.ClienteResponseDto;
import lombok.Data;

@Data
public class ClienteDataSourceResponseDto {
    private Long codigo;
    private String codigoDataSource;
    private ClienteResponseDto cliente;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
