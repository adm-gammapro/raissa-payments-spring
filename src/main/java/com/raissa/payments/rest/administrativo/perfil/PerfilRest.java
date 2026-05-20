package com.raissa.payments.rest.administrativo.perfil;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.administrativo.request.PerfilRequestDto;
import com.raissa.payments.domain.dto.administrativo.request.PerfilSearchDto;
import com.raissa.payments.domain.dto.administrativo.response.PerfilResponseDto;
import com.raissa.payments.domain.dto.administrativo.response.PerfilSearchResponseDto;
import com.raissa.payments.service.administrativo.perfil.PerfilService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("perfil")
@RequiredArgsConstructor
public class PerfilRest {
    private final PerfilService perfilService;

    @PostMapping("/registrar-perfil")
    public ResponseEntity<PerfilResponseDto> createPerfil(@Valid @RequestBody PerfilRequestDto requestDto, HttpServletRequest request) {
        PerfilResponseDto p = perfilService.registrar(requestDto, request);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/actualizar-perfil")
    public ResponseEntity<PerfilResponseDto> updatePerfil(@Valid @RequestBody PerfilRequestDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(perfilService.actualizar(requestDto, request));
    }

    @PostMapping("/eliminar-perfil/{codigo}")
    public ResponseEntity<PerfilResponseDto> deletePerfil(@PathVariable Long codigo, HttpServletRequest request) {
        return ResponseEntity.ok(perfilService.eliminar(codigo, request));
    }

    @PostMapping("/listarPerfil")
    public ResponseEntity<PerfilSearchResponseDto> listarPerfilesPage(@RequestBody(required = false) PerfilSearchDto perfilSearch) {
        return ResponseEntity.ok(perfilService.getPerfilesPage(perfilSearch));
    }

    @GetMapping("/obtenerPerfil")
    public ResponseEntity<PerfilResponseDto> obtenerPerfil(@RequestParam(name = "codigoPerfil", required = false) Long codigoPerfil) {
        return ResponseEntity.ok(perfilService.getPerfil(codigoPerfil));
    }
}
