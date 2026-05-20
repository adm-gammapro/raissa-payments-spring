package com.raissa.payments.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@Slf4j
public class PasswordGeneratorService {
    private static final SecureRandom random = new SecureRandom();

    /**
     * Genera password con requisitos bancarios:
     * - Mínimo 8 caracteres
     * - Al menos 1 mayúscula
     * - Al menos 1 minúscula
     * - Al menos 1 número
     * - Al menos 1 carácter especial
     * - Sin caracteres ambiguos (0, O, I, l, etc.)
     */
    public String generarPasswordBancario() {
        String mayusculas = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String minusculas = "abcdefghijkmnopqrstuvwxyz";
        String numeros = "23456789";
        String simbolos = "!@#$%&*";

        StringBuilder password = new StringBuilder();

        // Asegurar al menos uno de cada tipo
        password.append(mayusculas.charAt(random.nextInt(mayusculas.length())));
        password.append(minusculas.charAt(random.nextInt(minusculas.length())));
        password.append(numeros.charAt(random.nextInt(numeros.length())));
        password.append(simbolos.charAt(random.nextInt(simbolos.length())));

        // Completar a 8 caracteres
        String todos = mayusculas + minusculas + numeros + simbolos;
        for (int i = password.length(); i < 8; i++) {
            password.append(todos.charAt(random.nextInt(todos.length())));
        }

        return mezclarString(password.toString());
    }

    private String mezclarString(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}
