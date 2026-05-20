package com.raissa.payments.rest.administrativo.generales;

import com.raissa.comun.general.dto.InstitucionFinancieraResponseDto;
import com.raissa.comun.general.dto.ResponseDTO;
import com.raissa.payments.domain.dto.commons.TipoDocumentoResponseDto;
import com.raissa.payments.service.administrativo.general.GeneralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("general")
public class GeneralRest {
    private final GeneralService generalService;
    
    /**
     * Devuelve la lista de instituciones financieras
     *
     * @return {@link List<InstitucionFinancieraResponseDto>}
     */
    @GetMapping("/list-institucion-financiera")
    public ResponseEntity<List<InstitucionFinancieraResponseDto>> listarInstitucionFinancieraEmpresa() {
        return ResponseEntity.ok(generalService.listarInstitucionFinanciera());
    }

    @GetMapping("/obtener-codigo-random")
    public ResponseEntity<String> obtenerCodigoRandom(@RequestParam String username,
                                                      @RequestParam String passwordPlano,
                                                      @RequestParam String modoEnvio) {
        return ResponseEntity.ok(generalService.obtenerCodigoRandom(username, passwordPlano, modoEnvio));
    }

    @GetMapping("/listarTipoDocumento")
    public ResponseEntity<List<TipoDocumentoResponseDto>> listarTipoDocumento() {

        List<TipoDocumentoResponseDto> retorno=  generalService.listarTipoDocumento();

        return ResponseEntity.ok(retorno);
    }
}
