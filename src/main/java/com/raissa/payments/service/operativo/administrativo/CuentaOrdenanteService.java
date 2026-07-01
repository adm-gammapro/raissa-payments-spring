package com.raissa.payments.service.operativo.administrativo;

import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CuentaOrdenanteConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CuentaOrdenanteResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CuentaOrdenanteService {
    CuentaOrdenanteConnectResponseDto listarCuentaOrdenantePage(CuentaOrdenanteSearchDto search);

    CuentaOrdenanteResponseDto getCuentaOrdenante(CuentaOrdenanteRequestDto get);

    CuentaOrdenanteResponseDto createCuentaOrdenante(CuentaOrdenanteRequestDto create, HttpServletRequest request);

    CuentaOrdenanteResponseDto updateCuentaOrdenante(CuentaOrdenanteRequestDto update, HttpServletRequest request);

    CuentaOrdenanteResponseDto deleteCuentaOrdenante(CuentaOrdenanteRequestDto delete, HttpServletRequest request);

    List<CuentaOrdenanteResponseDto> listCuentaOrdenante(CuentaOrdenanteRequestDto list);
}