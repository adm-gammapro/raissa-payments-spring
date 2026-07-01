package com.raissa.payments.domain.dto.operativo.administrativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CuentaOrdenanteSearchConnectDto  extends SearchRequestDTO {
    private String numeroCuentaOrdenante;
    private String estadoRegistro;
}
