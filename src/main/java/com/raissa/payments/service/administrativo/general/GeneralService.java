package com.raissa.payments.service.administrativo.general;

import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;
import com.raissa.payments.domain.dto.commons.EstadoSolicitudDto;
import com.raissa.payments.domain.dto.commons.TipoDocumentoResponseDto;

import java.util.List;

public interface GeneralService {
    List<InstitucionFinancieraResponseDto> listarInstitucionFinanciera();

    String obtenerCodigoRandom(String username, String passwordPlano, String modoEnvio);

    List<TipoDocumentoResponseDto> listarTipoDocumento();

    List<EstadoSolicitudDto> listarEstadosActivos();

    String obtenerNombreInstitucionFinanciera(String codigo);

    String obtenerNombreInstitucionPorCodigoSbs(String codigoSbs);

    InstitucionFinancieraResponseDto obtenerInstitucionPorCodigo(String codigo);
}
