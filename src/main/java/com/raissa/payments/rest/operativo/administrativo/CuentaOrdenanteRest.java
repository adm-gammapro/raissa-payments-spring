package com.raissa.payments.rest.operativo.administrativo;

import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CuentaOrdenanteSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CuentaOrdenanteConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CuentaOrdenanteResponseDto;
import com.raissa.payments.service.operativo.administrativo.CuentaOrdenanteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/administrativo")
@RequiredArgsConstructor
public class CuentaOrdenanteRest {
    private final CuentaOrdenanteService cuentaOrdenanteService;

    /**
     * Lista paginada de cuentas ordenantes
     */
    @PostMapping("/list-page-cuenta-ordenante")
    public ResponseEntity<CuentaOrdenanteConnectResponseDto> listarCuentaOrdenantePage(@RequestBody CuentaOrdenanteSearchDto search) {
        return ResponseEntity.ok(cuentaOrdenanteService.listarCuentaOrdenantePage(search));
    }

    /**
     * Devuelve una cuenta ordenante por código
     */
    @PostMapping("/get-cuenta-ordenante")
    public ResponseEntity<CuentaOrdenanteResponseDto> getCuentaOrdenante(@RequestBody CuentaOrdenanteRequestDto get) {
        return ResponseEntity.ok(cuentaOrdenanteService.getCuentaOrdenante(get));
    }

    /**
     * Registra una nueva cuenta ordenante
     */
    @PostMapping("/create-cuenta-ordenante")
    public ResponseEntity<CuentaOrdenanteResponseDto> createCuentaOrdenante(@RequestBody CuentaOrdenanteRequestDto create,
                                                                            HttpServletRequest request) {
        return ResponseEntity.ok(cuentaOrdenanteService.createCuentaOrdenante(create, request));
    }

    /**
     * Actualiza una cuenta ordenante
     */
    @PostMapping("/update-cuenta-ordenante")
    public ResponseEntity<CuentaOrdenanteResponseDto> updateCuentaOrdenante(@RequestBody CuentaOrdenanteRequestDto update,
                                                                            HttpServletRequest request) {
        return ResponseEntity.ok(cuentaOrdenanteService.updateCuentaOrdenante(update, request));
    }

    /**
     * Elimina de forma lógica una cuenta ordenante
     */
    @PostMapping("/delete-cuenta-ordenante")
    public ResponseEntity<CuentaOrdenanteResponseDto> deleteCuentaOrdenante(@RequestBody CuentaOrdenanteRequestDto delete,
                                                                            HttpServletRequest request) {
        return ResponseEntity.ok(cuentaOrdenanteService.deleteCuentaOrdenante(delete, request));
    }

    /**
     * Lista todas las cuentas ordenantes activas
     */
    @PostMapping("/list-cuenta-ordenante")
    public ResponseEntity<List<CuentaOrdenanteResponseDto>> listCuentaOrdenante(@RequestBody CuentaOrdenanteRequestDto list) {
        return ResponseEntity.ok(cuentaOrdenanteService.listCuentaOrdenante(list));
    }
}