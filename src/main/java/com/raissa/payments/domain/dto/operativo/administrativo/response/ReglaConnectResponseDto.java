package com.raissa.payments.domain.dto.operativo.administrativo.response;

import com.raissa.comun.general.dto.SearchResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReglaConnectResponseDto extends SearchResponseDTO {
    List<ReglaResponseDto> list;

    public ReglaConnectResponseDto(int totalPages,
                                   long totalElements,
                                   int pageNumber,
                                   int rowPages,
                                   List<ReglaResponseDto> list) {
        super(totalPages, totalElements, pageNumber, rowPages);
        this.list = list;
    }
}
