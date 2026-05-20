package com.raissa.payments.service.administrativo.usuario;

import com.raissa.payments.domain.dto.administrativo.request.usuarioperfil.TransferirPerfilesRequestDto;
import com.raissa.payments.domain.dto.administrativo.response.usuarioperfil.UsuarioPerfilesTransferDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UsuarioPerfilService {
    UsuarioPerfilesTransferDto obtenerPerfilesParaTransferencia(Long usuarioId);

    void transferirPerfiles(TransferirPerfilesRequestDto transfer, HttpServletRequest request);
}
