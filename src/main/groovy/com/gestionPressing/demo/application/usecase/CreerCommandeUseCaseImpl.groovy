package com.gestionPressing.demo.application.usecase

import com.gestionPressing.demo.domain.events.CommandeEventV1
import com.gestionPressing.demo.domain.events.NotificationEventV1
import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.ports.input.CreerCommandeUseCase
import com.gestionPressing.demo.domain.ports.output.AuditEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeRepositoryPort
import com.gestionPressing.demo.domain.ports.output.NotificationEventPublisherPort
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * ═══════════════════════════════════════════════════════════
 *  APPLICATION — Use Case : Créer une commande
 *  Couche  : application/usecase
 *
 *  Rôle : orchestrer le domaine + appeler les ports de sortie.
 *  ⚠️  Ne contient PAS de logique métier.
 *      La logique métier est dans Commande (domaine).
 * ═══════════════════════════════════════════════════════════
 */
@Slf4j
@Service
@Transactional
class CreerCommandeUseCaseImpl implements CreerCommandeUseCase {

    private final CommandeRepositoryPort commandeRepository
    private final CommandeEventPublisherPort commandeEventPublisher
    private final NotificationEventPublisherPort notificationPublisher
    private final AuditEventPublisherPort auditPublisher

    CreerCommandeUseCaseImpl(CommandeRepositoryPort commandeRepository,
                             CommandeEventPublisherPort commandeEventPublisher,
                             NotificationEventPublisherPort notificationPublisher,
                             AuditEventPublisherPort auditPublisher) {
        this.commandeRepository     = commandeRepository
        this.commandeEventPublisher = commandeEventPublisher
        this.notificationPublisher  = notificationPublisher
        this.auditPublisher         = auditPublisher
    }

    @Override
    Commande creer(String clientId,
                   String clientEmail,
                   String clientTelephone,
                   String description,
                   Double montantTotal,
                   String acteurId) {

        String correlationId = UUID.randomUUID().toString()
        log.info("[{}] Création commande - client: {}", correlationId, clientId)

        // 1. Créer l'agrégat (logique dans le domaine)
        def commande = new Commande(
                UUID.randomUUID().toString(),
                clientId, clientEmail, clientTelephone
        )
        commande.description  = description
        commande.montantTotal = montantTotal

        // 2. Persister (via port → adaptateur PostgreSQL)
        def commandeSauvegardee = commandeRepository.sauvegarder(commande)

        // 3. Publier événement Kafka pressing.commande.events
        commandeEventPublisher.publier(new CommandeEventV1(
                eventType            : CommandeEventV1.EventType.COMMANDE_CREEE.name(),
                correlationId        : correlationId,
                commandeId           : commandeSauvegardee.id,
                clientId             : clientId,
                clientEmail          : clientEmail,
                clientTelephone      : clientTelephone,
                nouveauStatut        : commandeSauvegardee.statut,
                actionParUtilisateur : acteurId,
                montantTotal         : montantTotal
        ))

        // 4. Notifier le client (pressing.notification.events)
        notificationPublisher.publier(new NotificationEventV1(
                correlationId          : correlationId,
                commandeId             : commandeSauvegardee.id,
                canal                  : NotificationEventV1.CanalNotification.SMS_ET_EMAIL,
                priorite               : NotificationEventV1.PrioriteNotification.HIGH,
                destinataireEmail      : clientEmail,
                destinataireTelephone  : clientTelephone,
                sujet                  : "Commande #${commandeSauvegardee.id} reçue",
                corps                  : "Votre commande a bien été enregistrée. Nous vous tiendrons informé.",
                typeEvenementSource    : CommandeEventV1.EventType.COMMANDE_CREEE.name()
        ))

        // 5. Audit (pressing.audit.events)
        auditPublisher.publier(new com.gestionPressing.demo.domain.events.AuditEventV1(
                correlationId : correlationId,
                acteurId      : acteurId,
                acteurType    : 'USER',
                action        : 'CREATION_COMMANDE',
                entiteType    : 'COMMANDE',
                entiteId      : commandeSauvegardee.id,
                resultat      : AuditEventV1.ActionResult.SUCCESS,
                message       : "Commande créée pour client ${clientId}",
                service       : 'pressing-api'
        ))

        log.info("[{}] Commande créée: {}", correlationId, commandeSauvegardee.id)
        commandeSauvegardee
    }
}
