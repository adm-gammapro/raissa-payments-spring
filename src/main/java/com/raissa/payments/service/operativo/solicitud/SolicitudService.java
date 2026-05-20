package com.raissa.payments.service.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.LiquidacionSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionFlujoSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.ObservacionRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.SolicitudSearchDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.TrackingRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.LiquidacionSolicitudResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.ObservacionResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.SolicitudSearchResponseDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.TrackingResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SolicitudService {
    SolicitudSearchResponseDto getFindSolicitudesPage(SolicitudSearchDto search);

    Long flujoSolicitud(FlujoSolicitudRequestDto flujo, HttpServletRequest request);

    List<TrackingResponseDto> listTracking(TrackingRequestDto dto);

    Long flujoSolicitudObservacion(ObservacionFlujoSolicitudRequestDto dto, HttpServletRequest request);

    List<ObservacionResponseDto> listObservacion(ObservacionRequestDto dto);

    LiquidacionSolicitudResponseDto getResumenLiquidacion(LiquidacionSolicitudRequestDto req);
}
