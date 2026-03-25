package com.raissa.payments.domain.dto.operativo.solicitud.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CargaSolicitudRequestDto {
    Long idEmpresa;
    String usuarioCarga;
    MultipartFile file;
}