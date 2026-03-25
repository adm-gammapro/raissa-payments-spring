package com.raissa.payments.service.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;

public interface SolicitudService {
    SolicitudSearchResponseDto getFindSolicitudesPage(SolicitudSearchDto search);
}
