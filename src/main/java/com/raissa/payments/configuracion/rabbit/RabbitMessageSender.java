package com.raissa.payments.configuracion.rabbit;

import com.raissa.comun.aplicacion.dto.payments.FlujoSolicitudComunRequestDto;
import com.raissa.comun.general.dto.InstitucionFinancieraComunDto;
import com.raissa.payments.domain.dto.operativo.solicitud.request.FlujoSolicitudConnectRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMessageSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${payments.queue.consulta.name}")
    private String consultaQueue;

    @Value("${payments.queue.confirmacion.name}")
    private String confirmacionQueue;

    /**
     * Envía una solicitud de VALIDACIÓN a la cola
     */
    public void enviarValidacionSolicitud(FlujoSolicitudConnectRequestDto dto) {
        log.info("📤 Enviando VALIDACIÓN a cola - SolicitudId: {}, Cliente: {}", dto.getSolicitudId(), dto.getCodigoCliente());

        try {
            List<InstitucionFinancieraComunDto> listInstituciones = dto.getListInstituciones().stream()
                    .map(i -> new InstitucionFinancieraComunDto(i.getCodigo(), i.getCodigoSbs()))
                    .toList();

            FlujoSolicitudComunRequestDto flujo = new FlujoSolicitudComunRequestDto(dto.getSolicitudId(),
                    dto.getUsuario(),
                    listInstituciones,
                    dto.getCodigoCliente(),
                    dto.getUsuarioAuditoria(),
                    dto.getTerminalAuditoria(),
                    dto.getIpAuditoria());

            rabbitTemplate.convertAndSend(consultaQueue, flujo);

            log.debug("Mensaje enviado a VALIDACIÓN, cola: {}, vhost: {}", consultaQueue, virtualHost);
        } catch (NoClassDefFoundError e) {
            log.error("Error crítico: Clase no encontrada en classpath - Revisar dependencias", e);
            throw new IllegalStateException("Configuración incorrecta: dependencia faltante", e);
        } catch (Exception e) {
            log.error("Error en envío a RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Error en proceso", e);
        }
    }

    /**
     * Envía una solicitud de EJECUCIÓN a la cola
     */
    public void enviarEjecucionSolicitud(FlujoSolicitudConnectRequestDto dto) {
        log.info("📤 Enviando EJECUCIÓN a cola - SolicitudId: {}, Cliente: {}", dto.getSolicitudId(), dto.getCodigoCliente());

        try {
            List<InstitucionFinancieraComunDto> listInstituciones = dto.getListInstituciones().stream()
                    .map(i -> new InstitucionFinancieraComunDto(i.getCodigo(), i.getCodigoSbs()))
                    .toList();

            FlujoSolicitudComunRequestDto flujo = new FlujoSolicitudComunRequestDto(dto.getSolicitudId(),
                    dto.getUsuario(),
                    listInstituciones,
                    dto.getCodigoCliente(),
                    dto.getUsuarioAuditoria(),
                    dto.getTerminalAuditoria(),
                    dto.getIpAuditoria());

            rabbitTemplate.convertAndSend(confirmacionQueue, flujo);

            log.debug("Mensaje enviado a EJECUCIÓN, cola: {}, vhost: {}", confirmacionQueue, virtualHost);
        } catch (Exception e) {
            log.error("❌ Error enviando mensaje: {}", e.getMessage(), e);
            throw new RuntimeException("Error enviando mensaje a RabbitMQ", e);
        }
    }
}
