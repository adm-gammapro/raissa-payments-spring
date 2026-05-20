package com.raissa.payments.domain.dto.administrativo.response;

import com.raissa.comun.general.dto.SearchResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UsuarioSearchResponseDto extends SearchResponseDTO {
    List<UsuarioResponseDto> list;

    public UsuarioSearchResponseDto(int totalPages,
                                      long totalElements,
                                      int pageNumber,
                                      int rowPages,
                                      List<UsuarioResponseDto> list) {
        super(totalPages, totalElements, pageNumber, rowPages);
        this.list = list;
    }
}
