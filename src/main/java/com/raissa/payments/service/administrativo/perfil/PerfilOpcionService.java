package com.raissa.payments.service.administrativo.perfil;

import com.raissa.payments.domain.dto.administrativo.request.perfilopcion.TransferirOpcionesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilopcion.PerfilOpcionesTransferDto;
import jakarta.servlet.http.HttpServletRequest;

public interface PerfilOpcionService {
    PerfilOpcionesTransferDto obtenerOpcionesParaTransferencia(Long perfilId);
    void transferirOpciones(TransferirOpcionesRequestDto transfer, HttpServletRequest request);
}
