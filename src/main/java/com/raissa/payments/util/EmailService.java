package com.raissa.payments.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void enviarCodigo(String destino, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("anunez@gammapro.pe");
        message.setTo(destino);
        message.setSubject("Código de verificación");
        message.setText("Tu código es: " + codigo);

        mailSender.send(message);
    }
}
