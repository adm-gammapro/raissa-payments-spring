package com.raissa.payments.service.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.TrackingResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SolicitudService {
    SolicitudSearchResponseDto getFindSolicitudesPage(SolicitudSearchDto search);

    Long flujoSolicitud(FlujoSolicitudRequestDto flujo, HttpServletRequest request);

    List<TrackingResponseDto> listTracking(TrackingRequestDto dto);
}
