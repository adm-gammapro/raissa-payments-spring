package com.raissa.payments.service.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.usuariocliente.TransferirClientesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariocliente.UsuarioClientesTransferDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UsuarioClienteService {
    UsuarioClientesTransferDto obtenerClientesParaTransferencia(Long usuarioId);

    void transferirClientes(TransferirClientesRequestDto transfer, HttpServletRequest request);
}
