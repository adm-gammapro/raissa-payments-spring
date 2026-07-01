package com.raissa.payments.service.administrativo.tarifario;


import com.raissa.payments.domain.dto.administrativo.request.tarifario.RegistrarConsumoRequest;

public interface TarifarioConsumoService {
    void registrarConsumo(String keyId,
                          String token,
                          RegistrarConsumoRequest request);
}