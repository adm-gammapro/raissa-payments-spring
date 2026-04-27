package com.raissa.payments.util;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.entity.Auditoria;
import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.StringUtil;
import com.raissa.payments.exception.commons.InvalidEstadoException;
import jakarta.servlet.http.HttpServletRequest;
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
}
