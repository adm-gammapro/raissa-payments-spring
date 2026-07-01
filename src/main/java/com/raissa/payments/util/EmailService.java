package com.raissa.payments.util;

import com.raissa.comun.util.Constante;
import com.raissa.payments.domain.dto.operativo.administrativo.response.ConstanciaPagoResponse;
import com.raissa.payments.domain.entity.commons.GenericCatalogoEntity;
import com.raissa.payments.domain.repository.commons.GenericCatalogoRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final GenericCatalogoRepository catalogRepository;

    public void enviarCodigo(String destino, String codigo) {
        try {
            // 1. Obtener configuración del correo desde la tabla parámetros
            ConfiguracionEmail config = cargarConfiguracionEmail(Constante.TABLA_EMAIL, Constante.CAMPO_AUTORIZACION);

            // 2. Construir el cuerpo del email
            String cuerpoEmail = construirCuerpoEmailCodigoVerificacion(config.getHeader(), config.getFooter(), codigo);

            // 3. Enviar correo con MimeMessage (soporta HTML)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(config.getFrom());
            helper.setTo(destino);
            helper.setSubject(config.getSubject());
            helper.setText(cuerpoEmail, true); // true = es HTML

            mailSender.send(message);
            log.info("Correo enviado exitosamente a: {}", destino);

        } catch (MessagingException e) {
            log.error("Error al enviar correo a: {}", destino, e);
            throw new RuntimeException("Error al enviar correo de verificación", e);
        }
    }

    public void enviarClaveUsuario(String destino, String tipoDocumento, String numeroDocumento, String claveTemporal) {
        try {
            // 1. Obtener configuración del correo desde la tabla parámetros
            ConfiguracionEmail config = cargarConfiguracionEmail(Constante.TABLA_EMAIL, Constante.CAMPO_CLAVE_USER);

            // 2. Construir el cuerpo del email
            String cuerpoEmail = construirCuerpoEmailClaveUser(config.getHeader(),
                    config.getFooter(),
                    tipoDocumento,
                    numeroDocumento,
                    claveTemporal);

            // 3. Enviar correo con MimeMessage (soporta HTML)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(config.getFrom());
            helper.setTo(destino);
            helper.setSubject(config.getSubject());
            helper.setText(cuerpoEmail, true); // true = es HTML

            mailSender.send(message);
            log.info("Correo enviado exitosamente a: {}", destino);

        } catch (MessagingException e) {
            log.error("Error al enviar correo a: {}", destino, e);
            throw new RuntimeException("Error al enviar correo de verificación", e);
        }
    }

    public void enviarConstancia(String destino, ConstanciaPagoResponse constancia) {
        try {
            // 1. Obtener configuración del correo desde la tabla parámetros
            ConfiguracionEmail config = cargarConfiguracionEmail(Constante.TABLA_EMAIL, Constante.CAMPO_CONSTANCIA_PAGO);

            // 2. Construir el cuerpo del email con los datos de la constancia
            String cuerpoEmail = construirCuerpoEmailConstancia(config.getHeader(), config.getFooter(), constancia);

            // 3. Enviar correo con MimeMessage (soporta HTML)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(config.getFrom());
            helper.setTo(destino);
            helper.setSubject(config.getSubject());
            helper.setText(cuerpoEmail, true); // true = es HTML

            mailSender.send(message);
            log.info("Constancia de pago enviada exitosamente a: {}", destino);

        } catch (MessagingException e) {
            log.error("Error al enviar constancia de pago a: {}", destino, e);
            throw new RuntimeException("Error al enviar constancia de pago", e);
        }
    }

    /**
     * Construye el cuerpo del email reemplazando variables
     */
    private String construirCuerpoEmailCodigoVerificacion(String header, String footer, String codigo) {
        // Reemplazar variables en el template
        String cuerpo = header;
        cuerpo = cuerpo.replace("{{codigo}}", codigo);
        cuerpo = cuerpo.replace("{{fecha}}", java.time.LocalDateTime.now().toString());

        if (footer != null && !footer.isEmpty()) {
            cuerpo += footer;
        }

        cuerpo = cuerpo.replace("{{anio}}", String.valueOf(java.time.Year.now().getValue()));

        return cuerpo;
    }

    /**
     * Construye el cuerpo del email reemplazando variables
     */
    private String construirCuerpoEmailClaveUser(String header, String footer, String tipoDocumento, String numeroDocumento, String claveTemporal) {
        // Reemplazar variables en el template
        String cuerpo = header;
        cuerpo = cuerpo.replace("{{tipo_documento}}", tipoDocumento);
        cuerpo = cuerpo.replace("{{numero_documento}}", numeroDocumento);
        cuerpo = cuerpo.replace("{{password}}", claveTemporal);
        cuerpo = cuerpo.replace("{{fecha}}", java.time.LocalDateTime.now().toString());

        if (footer != null && !footer.isEmpty()) {
            cuerpo += footer;
        }

        cuerpo = cuerpo.replace("{{anio}}", String.valueOf(java.time.Year.now().getValue()));

        return cuerpo;
    }

    /**
     * Carga la configuración del email desde la tabla genérica
     */
    private ConfiguracionEmail cargarConfiguracionEmail(String tabla, String campo) {
        ConfiguracionEmail config = new ConfiguracionEmail();

        // Cargar FROM
        Optional<GenericCatalogoEntity> fromEntity = catalogRepository
                .findByIdTablaConstanteAndIdCampoConstanteAndIdValorConstante(
                        tabla,
                        campo,
                        Constante.CAMPO_FROM);

        config.setFrom(fromEntity
                .map(GenericCatalogoEntity::getDescripcion)
                .orElse("soporte@gammapro.pe")); // Valor por defecto

        // Cargar SUBJECT
        Optional<GenericCatalogoEntity> subjectEntity = catalogRepository
                .findByIdTablaConstanteAndIdCampoConstanteAndIdValorConstante(
                        tabla,
                        campo,
                        Constante.CAMPO_SUBJECT);

        config.setSubject(subjectEntity
                .map(GenericCatalogoEntity::getDescripcion)
                .orElse("Código de verificación")); // Valor por defecto

        // Cargar HEADER (HTML del mail)
        Optional<GenericCatalogoEntity> headerEntity = catalogRepository
                .findByIdTablaConstanteAndIdCampoConstanteAndIdValorConstante(
                        tabla,
                        campo,
                        Constante.CAMPO_BODY);

        config.setHeader(headerEntity
                .map(GenericCatalogoEntity::getDescripcion)
                .orElse(getDefaultHtmlTemplate())); // Template por defecto

        // Cargar FOOTER (opcional)
        Optional<GenericCatalogoEntity> footerEntity = catalogRepository
                .findByIdTablaConstanteAndIdCampoConstanteAndIdValorConstante(
                        tabla,
                        campo,
                        Constante.CAMPO_FOOTER);

        config.setFooter(footerEntity
                .map(GenericCatalogoEntity::getDescripcion)
                .orElse(""));

        return config;
    }

    /**
     * Construye el cuerpo del email con los datos de la constancia
     */
    private String construirCuerpoEmailConstancia(String header, String footer, ConstanciaPagoResponse constancia) {
        String cuerpo = header;

        // Reemplazar variables de la constancia
        cuerpo = cuerpo.replace("{{codigo_operacion}}", nvl(constancia.getCodigoOperacion()));
        cuerpo = cuerpo.replace("{{banco_origen}}", nvl(constancia.getBancoOrigen()));
        cuerpo = cuerpo.replace("{{cuenta_origen}}", nvl(constancia.getCuentaOrigen()));
        cuerpo = cuerpo.replace("{{destinatario}}", nvl(constancia.getDestinatario()));
        cuerpo = cuerpo.replace("{{entidad_destino}}", nvl(constancia.getEntidadDestino()));
        cuerpo = cuerpo.replace("{{destino}}", nvl(constancia.getDestino()));
        cuerpo = cuerpo.replace("{{moneda}}", nvl(constancia.getMoneda()));
        cuerpo = cuerpo.replace("{{monto}}", nvl(constancia.getMonto()));
        cuerpo = cuerpo.replace("{{fecha}}", nvl(constancia.getFecha()));
        cuerpo = cuerpo.replace("{{anio}}", String.valueOf(java.time.Year.now().getValue()));

        if (footer != null && !footer.isEmpty()) {
            footer = footer.replace("{{anio}}", String.valueOf(java.time.Year.now().getValue()));
            cuerpo += footer;
        }

        return cuerpo;
    }

    /**
     * Retorna el valor o string vacío si es null
     */
    private String nvl(String value) {
        return value != null ? value : "";
    }

    /**
     * Template HTML por defecto en caso no exista en la BD
     */
    private String getDefaultHtmlTemplate() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                    .container { max-width: 500px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .code { font-size: 32px; font-weight: bold; color: #4CAF50; text-align: center; padding: 20px; background-color: #f0f0f0; border-radius: 5px; letter-spacing: 5px; }
                    .footer { margin-top: 30px; text-align: center; color: #888888; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>¡Hola!</h2>
                    <p>Tu código de verificación es:</p>
                    <div class="code">{{codigo}}</div>
                </div>
            </body>
            </html>
            """;
    }

    /**
     * Clase interna para encapsular la configuración
     */
    @Data
    private static class ConfiguracionEmail {
        private String from;
        private String subject;
        private String header;
        private String footer;
    }
}
