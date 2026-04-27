package com.raissa.payments.service.operativo.administrativo;

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
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AdministrativoService {
    /**
     * Lista paginada de tipos de pago
     * @param search datos de busqueda
     * @return {@link TipoPagoConnectResponseDto}
     */
    TipoPagoConnectResponseDto listarTipoPagoPage(TipoPagoSearchDto search);

    /**
     * Devuelve un tipo de pago por codigo
     * @param get datos de busqueda
     * @return {@link TipoPagoResponseDto}
     */
    TipoPagoResponseDto getTipopago(TipoPagoRequestDto get);

    /**
     * Registra un nuevo tipo de pago
     * @param create Datos de registro
     * @return {@link TipoPagoResponseDto}
     */
    TipoPagoResponseDto createTipoPago(TipoPagoRequestDto create,
                                       HttpServletRequest request);

    /**
     * Actualiza un tipo de pago
     * @param update Datos de actualizacion
     * @return {@link TipoPagoResponseDto}
     */
    TipoPagoResponseDto updateTipoPago(TipoPagoRequestDto update,
                                       HttpServletRequest request);

    /**
     * Elimina de forma logica un tipo de pago
     * @param delete Datos de eliminacion
     * @return {@link TipoPagoResponseDto}
     */
    TipoPagoResponseDto deleteTipoPago(TipoPagoRequestDto delete,
                                       HttpServletRequest request);

    List<TipoPagoResponseDto> listTipopago(TipoPagoRequestDto get);

    /**
     * Lista paginada de categorias
     * @param search datos de busqueda
     * @return {@link CategoriaConnectResponseDto}
     */
    CategoriaConnectResponseDto listarCategoriaPage(CategoriaSearchDto search);

    /**
     * Devuelve una Categoria por codigo
     * @param get datos de busqueda
     * @return {@link CategoriaResponseDto}
     */
    CategoriaResponseDto getCategoria(CategoriaRequestDto get);

    /**
     * Registra una nueva Categoria
     * @param create Datos de registro
     * @return {@link CategoriaResponseDto}
     */
    CategoriaResponseDto createCategoria(CategoriaRequestDto create,
                                         HttpServletRequest request);

    /**
     * Actualiza una Categoria
     * @param update Datos de actualizacion
     * @return {@link CategoriaResponseDto}
     */
    CategoriaResponseDto updateCategoria(CategoriaRequestDto update,
                                         HttpServletRequest request);

    /**
     * Elimina de forma logica una Categoria
     * @param delete Datos de eliminacion
     * @return {@link CategoriaResponseDto}
     */
    CategoriaResponseDto deleteCategoria(CategoriaRequestDto delete,
                                         HttpServletRequest request);

    List<CategoriaResponseDto> listCategoria(CategoriaRequestDto get);

    VinculoCategoriaUsuarioResponseDto listVinculoCategoriaUsuario(VinculoCategoriaUsuarioRequestDto req);

    Boolean vincularCategoriaUsuario(CategoriaUsuarioRequestDto req,
                                     HttpServletRequest request);

    Boolean desvincularCategoriaUsuario(CategoriaUsuarioRequestDto req,
                                        HttpServletRequest request);

    /**
     * Lista paginada de reglas
     * @param search datos de busqueda
     * @return {@link ReglaConnectResponseDto}
     */
    ReglaConnectResponseDto listarReglaPage(ReglaSearchDto search);

    /**
     * Devuelve reglas por codigo
     * @param get datos de busqueda
     * @return {@link ReglaResponseDto}
     */
    ReglaResponseDto getRegla(ReglaRequestDto get);

    /**
     * Registra una nueva regla
     * @param create Datos de registro
     * @return {@link ReglaResponseDto}
     */
    ReglaResponseDto createRegla(ReglaRequestDto create,
                                 HttpServletRequest request);

    /**
     * Actualiza una regla
     * @param update Datos de actualizacion
     * @return {@link ReglaResponseDto}
     */
    ReglaResponseDto updateRegla(ReglaRequestDto update,
                                 HttpServletRequest request);

    /**
     * Elimina de forma logica una regla
     * @param delete Datos de eliminacion
     * @return {@link ReglaResponseDto}
     */
    ReglaResponseDto deleteRegla(ReglaRequestDto delete,
                                 HttpServletRequest request);

    List<ReglaResponseDto> listRegla(ReglaRequestDto get);


    ConfiguracionReglaConnectResponseDto listarConfiguracionReglaPage(ConfiguracionReglaSearchDto search);

    ConfiguracionReglaResponseDto getConfiguracionRegla(ConfiguracionReglaRequestDto get);

    ConfiguracionReglaResponseDto createConfiguracionRegla(ConfiguracionReglaRequestDto create,
                                                           HttpServletRequest request);

    ConfiguracionReglaResponseDto updateConfiguracionRegla(ConfiguracionReglaRequestDto update,
                                                           HttpServletRequest request);

    ConfiguracionReglaResponseDto deleteConfiguracionRegla(ConfiguracionReglaRequestDto delete,
                                                           HttpServletRequest request);
}
