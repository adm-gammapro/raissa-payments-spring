package com.raissa.payments.util;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.enums.commons.SortOrderEnum;
import com.raissa.comun.general.dto.SearchRequestDTO;
import com.raissa.comun.general.entity.Auditoria;
import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.StringUtil;
import com.raissa.payments.exception.commons.InvalidEstadoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.SecureRandom;
import java.util.Set;

public class AbstractRaissaPaymentsService extends AbstractService {
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaimAsString("username");
        }

        return "anonymous";
    }

    public static void setInsAuditFields(Auditoria entity, HttpServletRequest request) {
        entity.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());
        entity.setAudiIp(request.getRemoteAddr());
        entity.setAudiNomTerminal(request.getRemoteHost());
        entity.setAudiIpMod(request.getRemoteAddr());
        entity.setAudiNomTerminalMod(request.getRemoteHost());
    }

    public static void setModAuditFields(Auditoria entity, HttpServletRequest request) {
        entity.setAudiIpMod(request.getRemoteAddr());
        entity.setAudiNomTerminalMod(request.getRemoteHost());
    }

    public static void deleteValidation(Auditoria entity) {
        if (StringUtil.equiv(entity.getEstadoRegistro(), EstadoRegistroEnum.NO_VIGENTE.getValor())) {
            throw new InvalidEstadoException(EstadoRegistroEnum.NO_VIGENTE.getDescripcion(), "Eliminar");
        }
    }

    protected String validarSortField(String sortField, Set<String> validFields, String defaultField) {
        if (sortField == null) {
            return defaultField;
        } else {
            return validFields.contains(sortField) ? sortField : defaultField;
        }
    }

    protected String generarCodigo(int longitud) {
        StringBuilder codigo = new StringBuilder(longitud);

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(CHARACTERS.length());
            codigo.append(CHARACTERS.charAt(index));
        }

        return codigo.toString();
    }

    protected String formatearNombre(String nombres, String apellido) {
        if (nombres == null || nombres.isEmpty()) {
            return apellido;
        }

        String inicial = nombres.substring(0, 1).toUpperCase();
        return inicial + ". " + apellido;
    }

    public static Pageable buildPageable(SearchRequestDTO searchDto) {
        int page = Math.max(0, searchDto.getPage());
        int size = searchDto.getSize() > 0 ? searchDto.getSize() : 5;
        String sortField = searchDto.getSortField() != null && !searchDto.getSortField().isBlank() ? searchDto.getSortField() : "id";
        Sort.Direction direction = Sort.Direction.ASC;
        if (searchDto.getSortOrder() != null) {
            direction = searchDto.getSortOrder() == SortOrderEnum.ASCENDENTE ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        return PageRequest.of(page, size, Sort.by(direction, new String[]{sortField}));
    }

    public static String enmascararTexto(String valorOriginal) {
        if (valorOriginal != null && valorOriginal.length() > 4) {
            String ultimosCuatro = valorOriginal.substring(valorOriginal.length() - 4);
            String asteriscos = "*".repeat(valorOriginal.length() - 4);

            String resultado = asteriscos + ultimosCuatro;

            return resultado;
        } else {
            return valorOriginal;
        }
    }
}
