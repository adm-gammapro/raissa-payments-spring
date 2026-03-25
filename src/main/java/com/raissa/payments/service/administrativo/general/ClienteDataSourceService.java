package com.raissa.payments.service.administrativo.general;

import com.raissa.payments.domain.dto.commons.ClienteDataSourceResponseDto;

public interface ClienteDataSourceService {
    /**
     * Devuelve un objeto que relaciona cliente con datasource
     *
     * @param codigoCliente identificador unico de cliente
     * @return {@link ClienteDataSourceResponseDto}
     */
    ClienteDataSourceResponseDto getDataSource(Long codigoCliente);
}
