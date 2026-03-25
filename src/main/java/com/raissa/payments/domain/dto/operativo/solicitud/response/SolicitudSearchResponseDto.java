package com.raissa.payments.domain.dto.operativo.solicitud.response;

import com.raissa.comun.general.dto.SearchResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SolicitudSearchResponseDto extends SearchResponseDTO {
    List<SolicitudResponseDto> list;

    public SolicitudSearchResponseDto(int totalPages,
                                      long totalElements,
                                      int pageNumber,
                                      int rowPages,
                                      List<SolicitudResponseDto> list) {
        super(totalPages, totalElements, pageNumber, rowPages);
        this.list = list;
    }
}