package com.raissa.payments.rest.operativo.administrativo;

import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.CategoriaUsuarioRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConfiguracionReglaRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ConfiguracionReglaSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ReglaRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.ReglaSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.TipoPagoRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.TipoPagoSearchDto;
import com.raissa.payments.domain.dto.operativo.administrativo.request.VinculoCategoriaUsuarioRequestDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CategoriaConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.CategoriaResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ConfiguracionReglaConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ConfiguracionReglaResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ReglaConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ReglaResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.TipoPagoConnectResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.TipoPagoResponseDto;
import com.raissa.payments.domain.dto.operativo.administrativo.response.VinculoCategoriaUsuarioResponseDto;
import com.raissa.payments.service.operativo.administrativo.AdministrativoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/administrativo")
@RequiredArgsConstructor
@Slf4j
public class AdministrativoRest {
    private final AdministrativoService administrativoService;

    /**
     * Lista paginada de tipos de pago
     * @param search datos de busqueda
     * @return {@link TipoPagoConnectResponseDto}
     */
    @PostMapping("/list-page-tipo-pago")
    public ResponseEntity<TipoPagoConnectResponseDto> listarTipoPagoPage(@RequestBody TipoPagoSearchDto search) {
        return ResponseEntity.ok(administrativoService.listarTipoPagoPage(search));
    }

    /**
     * Devuelve un tipo de pago por codigo
     * @param get datos de busqueda
     * @return {@link TipoPagoResponseDto}
     */
    @PostMapping("/get-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> getTipopago(@RequestBody TipoPagoRequestDto get) {
        return ResponseEntity.ok(administrativoService.getTipopago(get));
    }

    /**
     * Registra un nuevo tipo de pago
     * @param create Datos de registro
     * @return {@link TipoPagoResponseDto}
     */
    @PostMapping("/create-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> createTipoPago(@RequestBody TipoPagoRequestDto create,
                                                              HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.createTipoPago(create, request));
    }

    /**
     * Actualiza un tipo de pago
     * @param update Datos de actualizacion
     * @return {@link TipoPagoResponseDto}
     */
    @PostMapping("/update-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> updateTipoPago(@RequestBody TipoPagoRequestDto update,
                                                              HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.updateTipoPago(update, request));
    }

    /**
     * Elimina de forma logica un tipo de pago
     * @param delete Datos de eliminacion
     * @return {@link TipoPagoResponseDto}
     */
    @PostMapping("/delete-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> deleteTipoPago(@RequestBody TipoPagoRequestDto delete,
                                                              HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.deleteTipoPago(delete, request));
    }

    @PostMapping("/list-tipo-pago")
    public ResponseEntity<List<TipoPagoResponseDto>> listTipopago(@RequestBody TipoPagoRequestDto list) {
        return ResponseEntity.ok(administrativoService.listTipopago(list));
    }

    /**
     * Lista paginada de categorias
     * @param search datos de busqueda
     * @return {@link CategoriaConnectResponseDto}
     */
    @PostMapping("/list-page-categoria")
    public ResponseEntity<CategoriaConnectResponseDto> listarCategoriaPage(@RequestBody CategoriaSearchDto search) {
        return ResponseEntity.ok(administrativoService.listarCategoriaPage(search));
    }

    /**
     * Devuelve una Categoria por codigo
     * @param get datos de busqueda
     * @return {@link CategoriaResponseDto}
     */
    @PostMapping("/get-categoria")
    public ResponseEntity<CategoriaResponseDto> getCategoria(@RequestBody CategoriaRequestDto get) {
        return ResponseEntity.ok(administrativoService.getCategoria(get));
    }

    /**
     * Registra una nueva Categoria
     * @param create Datos de registro
     * @return {@link CategoriaResponseDto}
     */
    @PostMapping("/create-categoria")
    public ResponseEntity<CategoriaResponseDto> createCategoria(@RequestBody CategoriaRequestDto create,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.createCategoria(create, request));
    }

    /**
     * Actualiza una Categoria
     * @param update Datos de actualizacion
     * @return {@link CategoriaResponseDto}
     */
    @PostMapping("/update-categoria")
    public ResponseEntity<CategoriaResponseDto> updateCategoria(@RequestBody CategoriaRequestDto update,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.updateCategoria(update, request));
    }

    /**
     * Elimina de forma logica una Categoria
     * @param delete Datos de eliminacion
     * @return {@link CategoriaResponseDto}
     */
    @PostMapping("/delete-categoria")
    public ResponseEntity<CategoriaResponseDto> deleteCategoria(@RequestBody CategoriaRequestDto delete,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.deleteCategoria(delete, request));
    }

    @PostMapping("/list-categoria")
    public ResponseEntity<List<CategoriaResponseDto>> listCategoria(@RequestBody CategoriaRequestDto list) {
        return ResponseEntity.ok(administrativoService.listCategoria(list));
    }

    @PostMapping("/list-vinculo-categoria-usuario")
    public ResponseEntity<VinculoCategoriaUsuarioResponseDto> listVinculoCategoriaUsuario(@RequestBody VinculoCategoriaUsuarioRequestDto req) {
        return ResponseEntity.ok(administrativoService.listVinculoCategoriaUsuario(req));
    }

    @PostMapping("/vincular-categoria-usuario")
    public ResponseEntity<Boolean> vincularCategoriaUsuario(@RequestBody CategoriaUsuarioRequestDto req,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.vincularCategoriaUsuario(req, request));
    }

    @PostMapping("/desvincular-categoria-usuario")
    public ResponseEntity<Boolean> desvincularCategoriaUsuario(@RequestBody CategoriaUsuarioRequestDto req,
                                                               HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.desvincularCategoriaUsuario(req, request));
    }

    /**
     * Lista paginada de reglas
     * @param search datos de busqueda
     * @return {@link ReglaConnectResponseDto}
     */
    @PostMapping("/list-page-regla")
    public ResponseEntity<ReglaConnectResponseDto> listarReglaPage(@RequestBody ReglaSearchDto search) {
        return ResponseEntity.ok(administrativoService.listarReglaPage(search));
    }

    /**
     * Devuelve reglas por codigo
     * @param get datos de busqueda
     * @return {@link ReglaResponseDto}
     */
    @PostMapping("/get-regla")
    public ResponseEntity<ReglaResponseDto> getRegla(@RequestBody ReglaRequestDto get) {
        return ResponseEntity.ok(administrativoService.getRegla(get));
    }

    /**
     * Registra una nueva regla
     * @param create Datos de registro
     * @return {@link ReglaResponseDto}
     */
    @PostMapping("/create-regla")
    public ResponseEntity<ReglaResponseDto> createRegla(@RequestBody ReglaRequestDto create,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.createRegla(create, request));
    }

    /**
     * Actualiza una regla
     * @param update Datos de actualizacion
     * @return {@link ReglaResponseDto}
     */
    @PostMapping("/update-regla")
    public ResponseEntity<ReglaResponseDto> updateRegla(@RequestBody ReglaRequestDto update,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.updateRegla(update, request));
    }

    /**
     * Elimina de forma logica una regla
     * @param delete Datos de eliminacion
     * @return {@link ReglaResponseDto}
     */
    @PostMapping("/delete-regla")
    public ResponseEntity<ReglaResponseDto> deleteRegla(@RequestBody ReglaRequestDto delete,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(administrativoService.deleteRegla(delete, request));
    }

    @PostMapping("/list-regla")
    public ResponseEntity<List<ReglaResponseDto>> listRegla(@RequestBody ReglaRequestDto list) {
        return ResponseEntity.ok(administrativoService.listRegla(list));
    }

    /**
     * Lista paginada de configuraciones de regla
     * @param search datos de búsqueda
     * @return {@link ConfiguracionReglaConnectResponseDto}
     */
    @PostMapping("/list-page-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaConnectResponseDto> listarConfiguracionReglaPage(@RequestBody ConfiguracionReglaSearchDto search) {
        log.info("Listando configuraciones de regla con filtros: {}", search);
        return ResponseEntity.ok(administrativoService.listarConfiguracionReglaPage(search));
    }

    /**
     * Obtiene una configuración de regla por código
     * @param get datos de búsqueda
     * @return {@link ConfiguracionReglaResponseDto}
     */
    @PostMapping("/get-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> getConfiguracionRegla(@RequestBody ConfiguracionReglaRequestDto get) {
        log.info("Obteniendo configuración de regla con código: {}", get.getCodigo());
        return ResponseEntity.ok(administrativoService.getConfiguracionRegla(get));
    }

    /**
     * Registra una nueva configuración de regla
     * @param create Datos de registro
     * @param request Información del request HTTP
     * @return {@link ConfiguracionReglaResponseDto}
     */
    @PostMapping("/create-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> createConfiguracionRegla(@RequestBody ConfiguracionReglaRequestDto create,
                                                                                  HttpServletRequest request) {
        log.info("Creando nueva configuración de regla: {}", create);
        return ResponseEntity.ok(administrativoService.createConfiguracionRegla(create, request));
    }

    /**
     * Actualiza una configuración de regla existente
     * @param update Datos de actualización
     * @param request Información del request HTTP
     * @return {@link ConfiguracionReglaResponseDto}
     */
    @PostMapping("/update-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> updateConfiguracionRegla(@RequestBody ConfiguracionReglaRequestDto update,
                                                                                  HttpServletRequest request) {
        log.info("Actualizando configuración de regla con código: {}", update.getCodigo());
        return ResponseEntity.ok(administrativoService.updateConfiguracionRegla(update, request));
    }

    /**
     * Elimina de forma lógica una configuración de regla
     * @param delete Datos de eliminación
     * @param request Información del request HTTP
     * @return {@link ConfiguracionReglaResponseDto}
     */
    @PostMapping("/delete-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> deleteConfiguracionRegla(@RequestBody ConfiguracionReglaRequestDto delete,
                                                                                  HttpServletRequest request) {
        log.info("Eliminando (lógicamente) configuración de regla con código: {}", delete.getCodigo());
        return ResponseEntity.ok(administrativoService.deleteConfiguracionRegla(delete, request));
    }
}
