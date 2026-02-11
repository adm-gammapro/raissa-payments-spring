package com.raissa.payments.exception.commons;

public class InvalidEstadoException extends RuntimeException {
    public InvalidEstadoException(String message) {
        super(message);
    }

    public InvalidEstadoException(String estado, String accion) {
        super("El estado del registro es '" + estado + "' y no es válido para la acción '" + accion + "'");
    }
}
