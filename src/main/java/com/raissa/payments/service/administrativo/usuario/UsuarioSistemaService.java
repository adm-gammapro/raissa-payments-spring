package com.raissa.payments.service.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.usuariosistema.TransferirSistemasRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuariosistema.UsuarioSistemasTransferDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UsuarioSistemaService {
    UsuarioSistemasTransferDto obtenerSistemasParaTransferencia(Long usuarioId);

    void transferirSistemas(TransferirSistemasRequestDto transfer, HttpServletRequest request);
}
