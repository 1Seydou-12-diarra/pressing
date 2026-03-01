package com.gestionPressing.demo.infrastructure.kafka.consumer

import com.gestionPressing.demo.domain.events.NotificationEventV1
import com.gestionPressing.demo.domain.ports.output.IdempotencePort
import com.gestionPressing.demo.infrastructure.kafka.config.KafkaTopicsConfig
import groovy.util.logging.Slf4j
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Slf4j
@Component
class NotificationConsumer {

    private final IdempotencePort idempotencePort

    NotificationConsumer(IdempotencePort idempotencePort) {
        this.idempotencePort = idempotencePort
    }

    // ─── Consumer principal ───────────────────────────────────
    @KafkaListener(
            topics = KafkaTopicsConfig.TOPIC_NOTIFICATION,
            groupId = KafkaTopicsConfig.GROUP_NOTIFICATION,
            containerFactory = "notificationListenerFactory"
    )
    void consommer(ConsumerRecord<String, NotificationEventV1> record,
                   Acknowledgment ack,
                   @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                   @Header(KafkaHeaders.OFFSET) long offset) {

        def event = record.value()

        log.info("[{}] 📩 Notification reçue - canal: {}, commandeId: {}, partition: {}, offset: {}",
                event.correlationId, event.canal, event.commandeId, partition, offset)

        if (idempotencePort.dejaTraite(event.eventId)) {
            log.warn("[{}] ⏭️ Skip - eventId déjà traité", event.correlationId)
            ack.acknowledge()
            return
        }

        try {
            envoyerNotification(event)
            idempotencePort.marquerCommeTraite(event.eventId)
            ack.acknowledge()

            log.info("[{}] ✅ Notification envoyée", event.correlationId)

        } catch (Exception e) {
            log.error("[{}] ❌ Échec notification", event.correlationId, e)
            throw e // retry → DLT
        }
    }

    // ─── DLT ────────────────────────────────────────────────
    @KafkaListener(
            topics = KafkaTopicsConfig.TOPIC_NOTIFICATION_DLT,
            groupId = KafkaTopicsConfig.GROUP_NOTIFICATION_DLT,
            containerFactory = "notificationListenerFactory"
    )
    void consommerDlt(ConsumerRecord<String, NotificationEventV1> record,
                      Acknowledgment ack) {

        def cause = record.headers().lastHeader('kafka_dlt-exception-message')
                ?.value()?.toString() ?: 'inconnue'

        log.error("💀 [DLT] Notification échouée - eventId: {}, cause: {}",
                record.value()?.eventId, cause)

        ack.acknowledge()
    }

    // ─── Dispatch ───────────────────────────────────────────
    private void envoyerNotification(NotificationEventV1 event) {
        switch (event.canal) {
            case NotificationEventV1.CanalNotification.SMS:
                envoyerSms(event); break
            case NotificationEventV1.CanalNotification.EMAIL:
                envoyerEmail(event); break
            case NotificationEventV1.CanalNotification.SMS_ET_EMAIL:
                envoyerSms(event)
                envoyerEmail(event)
                break
        }
    }

    private void envoyerSms(NotificationEventV1 event) {
        log.info("📱 SMS → {} : {}", event.destinataireTelephone, event.corps)
    }

    private void envoyerEmail(NotificationEventV1 event) {
        log.info("📧 Email → {} | {} : {}",
                event.destinataireEmail, event.sujet, event.corps)
    }
}