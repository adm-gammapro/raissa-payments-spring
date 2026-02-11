package com.raissa.payments.service.administrativo.cliente;

import com.raissa.payments.domain.dto.administrativo.response.ClienteResponseDto;

public interface ClienteService {
    ClienteResponseDto obtenerCliente(Long codigoCliente);
}
