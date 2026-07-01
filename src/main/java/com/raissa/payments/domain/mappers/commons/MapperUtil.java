package com.raissa.payments.domain.mappers.commons;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class MapperUtil {
    /**
     * Método auxiliar para convertir String a EstadoRegistroEnum.
     */
    @Named("mapStringToEstadoRegistroEnum")
    public EstadoRegistroEnum mapStringToEstadoRegistroEnum(String estado) {
        return estado != null ? EstadoRegistroEnum.fromValor(estado) : null;
    }

    /**
     * Método personalizado para convertir LocalDateTime a String
     */
    @Named("mapLocalDateTimeToString")
    public String mapLocalDateTimeToString(LocalDateTime date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
    }

    /**
     * Método personalizado para convertir LocalDate a String
     */
    @Named("mapLocalDateToString")
    public String mapLocalDateToString(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
    }

    /**
     * Método personalizado para convertir LocalDate a String con formato yyyy-mm-dd
     */
    @Named("mapLocalDateToStringInternacional")
    public String mapLocalDateToStringInternacional(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }

    /**
     * Método personalizado para convertir String con formato yyyy-mm-dd a LocalDate
     */
    @Named("mapStringToLocalDateInternacional")
    public LocalDate mapStringToLocalDateInternacional(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            log.error("Error al parsear fecha: {}, formato esperado: yyyy-MM-dd", dateStr, e);
            return null;
        }
    }

    /**
     * Método personalizado para convertir String a LocalDate
     */
    @Named("emptyToNull")
    public LocalDate emptyToNull(String s) {
        if (s == null || s.isEmpty()) return null;
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
