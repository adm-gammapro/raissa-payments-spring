package com.raissa.payments.rest.administrativo.cliente;

import com.raissa.payments.domain.dto.administrativo.response.ClienteResponseDto;
import com.raissa.payments.service.administrativo.cliente.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cliente")
@RequiredArgsConstructor
public class ClienteRest {
    private final ClienteService clienteService;
    @GetMapping("/obtenerCliente")
    public ResponseEntity<ClienteResponseDto> obtenerCliente (@RequestParam(name = "idEmpresa", required = false) Long idEmpresa) {

        return ResponseEntity.ok(clienteService.obtenerCliente(idEmpresa));
    }
}
