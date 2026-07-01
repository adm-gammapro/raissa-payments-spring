package com.raissa.payments.domain.dto.operativo.administrativo.response;

import com.raissa.comun.general.dto.SearchResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CuentaOrdenanteConnectResponseDto extends SearchResponseDTO {
    private List<CuentaOrdenanteResponseDto> list;

    public CuentaOrdenanteConnectResponseDto(int totalPages,
                                             long totalElements,
                                             int pageNumber,
                                             int rowPages,
                                             List<CuentaOrdenanteResponseDto> list) {
        super(totalPages, totalElements, pageNumber, rowPages);
        this.list = list;
    }
}
