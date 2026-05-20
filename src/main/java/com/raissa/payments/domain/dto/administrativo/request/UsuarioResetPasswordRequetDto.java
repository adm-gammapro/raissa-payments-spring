package com.raissa.payments.domain.dto.administrativo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResetPasswordRequetDto {
    private Long id;
    private String password;
}