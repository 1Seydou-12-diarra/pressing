package com.gestionPressing.demo.infrastructure.kafka.producer

import com.gestionPressing.demo.domain.events.AuditEventV1
import com.gestionPressing.demo.domain.events.CommandeEventV1
import com.gestionPressing.demo.domain.events.NotificationEventV1
import com.gestionPressing.demo.domain.ports.output.AuditEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.NotificationEventPublisherPort
import groovy.util.logging.Slf4j
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit

/**
 * ═══════════════════════════════════════════════════════════
 *  INFRASTRUCTURE — Adaptateurs de SORTIE (Producers Kafka)
 *  Couche : infrastructure/kafka/producer
 *
 *  Chaque classe implémente UN port de sortie du domaine.
 *  Le domaine ne sait pas que c'est Kafka derrière.
 * ═══════════════════════════════════════════════════════════
 */

// ─── Producer : pressing.commande.events ─────────────────────────────────────
@Slf4j
@Component
class CommandeKafkaProducer implements CommandeEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate

    CommandeKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate
    }

    @Override
    void publier(CommandeEventV1 event) {
        // Clé = commandeId → ordre garanti par partition pour une commande
        kafkaTemplate.send(KafkaTopicsConfig.TOPIC_COMMANDE, event.commandeId, event)
                .whenComplete { result, ex ->
                    if (ex) {
                        log.error("[{}] ❌ Échec publication commande - commandeId: {}, eventId: {}",
                                event.correlationId, event.commandeId, event.eventId, ex)
                    } else {
                        log.info("[{}] ✅ Événement commande publié - type: {}, partition: {}, offset: {}",
                                event.correlationId,
                                event.eventType,
                                result.recordMetadata.partition(),
                                result.recordMetadata.offset())
                    }
                }
    }

    @Override
    void publierSync(CommandeEventV1 event) {
        try {
            def result = kafkaTemplate
                    .send(KafkaTopicsConfig.TOPIC_COMMANDE, event.commandeId, event)
                    .get(10, TimeUnit.SECONDS)

            log.info("[{}] ✅ Publication synchrone OK - partition: {}, offset: {}",
                    event.correlationId,
                    result.recordMetadata.partition(),
                    result.recordMetadata.offset())

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt()
            throw new RuntimeException("Publication interrompue", e)
        } catch (Exception e) {
            throw new RuntimeException("Échec publication synchrone commande", e)
        }
    }
}

// ─── Producer : pressing.notification.events ─────────────────────────────────
@Slf4j
@Component
class NotificationKafkaProducer implements NotificationEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate

    NotificationKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate
    }

    @Override
    void publier(NotificationEventV1 event) {
        String key = event.commandeId ?: event.eventId
        kafkaTemplate.send(KafkaTopicsConfig.TOPIC_NOTIFICATION, key, event)
                .whenComplete { result, ex ->
                    if (ex) {
                        log.error("[{}] ❌ Échec notification - eventId: {}",
                                event.correlationId, event.eventId, ex)
                    } else {
                        log.info("[{}] ✅ Notification publiée - canal: {}, partition: {}, offset: {}",
                                event.correlationId,
                                event.canal,
                                result.recordMetadata.partition(),
                                result.recordMetadata.offset())
                    }
                }
    }
}

// ─── Producer : pressing.audit.events ────────────────────────────────────────
@Slf4j
@Component
class AuditKafkaProducer implements AuditEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate

    AuditKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate
    }

    @Override
    void publier(AuditEventV1 event) {
        String key = event.entiteId ?: event.eventId
        kafkaTemplate.send(KafkaTopicsConfig.TOPIC_AUDIT, key, event)
                .whenComplete { result, ex ->
                    if (ex) {
                        log.error("[{}] ❌ Échec audit - action: {}",
                                event.correlationId, event.action, ex)
                    } else {
                        log.debug("[{}] ✅ Audit publié - action: {}, entite: {}",
                                event.correlationId, event.action, event.entiteId)
                    }
                }
    }
}