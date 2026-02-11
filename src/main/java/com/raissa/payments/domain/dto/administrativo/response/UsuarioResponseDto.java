package com.raissa.payments.domain.dto.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Long id;
    private String username;
    private String nombres;
    private String apePaterno;
    private String apeMaterno;
    private String password;
    private String fechaCambioClave;
    private String indicadorExpiracion;
    private String fechaExpiracionClave;
    private String correo;
    private String telefono;
    private String codigoTipoDocumento;
    private String descripcionTipoDocumento;
    private String numeroDocumento;
    private String tipoUsuario;
    private String claseUsuario;
    private EstadoRegistroEnum estadoRegistro;
    private String idEmpresa;
    private String audiFechIns;
}
