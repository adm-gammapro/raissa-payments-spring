package com.raissa.payments.service.administrativo.perfil;

import com.raissa.payments.domain.dto.administrativo.request.perfilcliente.TransferirClientesPerfilRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.perfilcliente.PerfilClientesTransferDto;
import jakarta.servlet.http.HttpServletRequest;

public interface PerfilClienteService {
    PerfilClientesTransferDto obtenerClientesParaTransferencia(Long perfilId);

    void transferirClientes(TransferirClientesPerfilRequestDto transfer, HttpServletRequest request);
}