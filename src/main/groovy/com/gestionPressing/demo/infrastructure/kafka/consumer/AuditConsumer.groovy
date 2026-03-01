package com.gestionPressing.demo.infrastructure.kafka.consumer

import com.gestionPressing.demo.domain.events.AuditEventV1
import com.gestionPressing.demo.domain.ports.output.IdempotencePort
import com.gestionPressing.demo.infrastructure.kafka.config.KafkaTopicsConfig
import groovy.util.logging.Slf4j
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Slf4j
@Component
class AuditConsumer {

    private final IdempotencePort idempotencePort

    AuditConsumer(IdempotencePort idempotencePort) {
        this.idempotencePort = idempotencePort
    }

    // ─── Consumer principal ──────────────────────────────────────
    @KafkaListener(
            topics           = KafkaTopicsConfig.TOPIC_AUDIT,
            groupId          = KafkaTopicsConfig.GROUP_AUDIT,
            containerFactory = "auditListenerFactory"
    )
    void consommer(ConsumerRecord<String, AuditEventV1> record, Acknowledgment ack) {
        def event = record.value()

        log.debug("[{}] 🔍 Audit reçu - action: {}, acteur: {}, entite: {}",
                event.correlationId, event.action, event.acteurId, event.entiteId)

        // Idempotence : skip si déjà traité
        if (idempotencePort.dejaTraite(event.eventId)) {
            log.warn("[{}] ⏭️ Skip audit dupliqué: {}", event.correlationId, event.eventId)
            ack.acknowledge()
            return
        }

        try {
            persisterLog(event)

            // Marquer comme traité AVANT ack
            idempotencePort.marquerCommeTraite(event.eventId)

            // ACK manuel
            ack.acknowledge()
            log.info("[{}] ✅ Audit persité - eventId: {}", event.correlationId, event.eventId)

        } catch (Exception e) {
            log.error("[{}] ❌ Échec persistance audit - eventId: {}",
                    event.correlationId, event.eventId, e)
            throw e
        }
    }

    // ─── Consumer DLT ────────────────────────────────────────────
    @KafkaListener(
            topics  = KafkaTopicsConfig.TOPIC_AUDIT_DLT,
            groupId = KafkaTopicsConfig.GROUP_AUDIT_DLT,
            containerFactory = "auditListenerFactory"
    )
    void consommerDlt(ConsumerRecord<String, AuditEventV1> record, Acknowledgment ack) {
        log.error("💀 [DLT] Audit en dead letter - eventId: {}", record.value()?.eventId)
        // auditLogRepository.sauvegarderFallback(record.value())
        ack.acknowledge()
    }

    // ─── Dispatch persistance ───────────────────────────────────
    private void persisterLog(AuditEventV1 event) {
        log.info("💾 Audit - [{}] par [{}] sur {}/{} → {}",
                event.action, event.acteurId, event.entiteType, event.entiteId, event.resultat)
        // auditLogRepository.save(AuditLog.from(event))
    }
}