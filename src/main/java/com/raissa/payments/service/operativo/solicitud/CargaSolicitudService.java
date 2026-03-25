package com.raissa.payments.service.operativo.solicitud;

import com.raissa.payments.domain.dto.operativo.solicitud.request.CargaSolicitudJsonRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.CargaSolicitudRequestDto;
import com.raissa.payments.domain.dto.operativo.solicitud.response.CargaSolicitudResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface CargaSolicitudService {
    CargaSolicitudResponseDto cargar(CargaSolicitudRequestDto req, HttpServletRequest request);

    CargaSolicitudResponseDto cargarDesdeJson(CargaSolicitudJsonRequestDto req, HttpServletRequest request);
}