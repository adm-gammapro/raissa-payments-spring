package com.raissa.payments.service.administrativo.general;

import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;

import java.util.List;

public interface GeneralService {
    /**
     * Devuelve la lista de instituciones financieras vinculadas al cliente
     *
     * @return {@link List<InstitucionFinancieraResponseDto>}
     */
    List<InstitucionFinancieraResponseDto> listarInstitucionFinanciera();

    String obtenerCodigoRandom(String username, String passwordPlano, String modoEnvio);
}
