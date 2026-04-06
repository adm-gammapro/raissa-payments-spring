package com.raissa.payments.domain.dto.operativo.administrativo.response;

import com.raissa.comun.general.dto.SearchResponseDTO;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudResponseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CategoriaConnectResponseDto extends SearchResponseDTO {
    List<CategoriaResponseDto> list;

    public CategoriaConnectResponseDto(int totalPages,
                                       long totalElements,
                                       int pageNumber,
                                       int rowPages,
                                       List<CategoriaResponseDto> list) {
        super(totalPages, totalElements, pageNumber, rowPages);
        this.list = list;
    }
}
