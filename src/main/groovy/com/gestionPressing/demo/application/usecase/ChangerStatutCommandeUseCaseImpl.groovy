package com.gestionPressing.demo.application.usecase

import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.events.AuditEventV1
import com.gestionPressing.demo.domain.events.CommandeEventV1
import com.gestionPressing.demo.domain.events.NotificationEventV1
import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.ports.input.ChangerStatutCommandeUseCase
import com.gestionPressing.demo.domain.ports.output.AuditEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeRepositoryPort
import com.gestionPressing.demo.domain.ports.output.NotificationEventPublisherPort
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service


/**
 * ═══════════════════════════════════════════════════════════
 *  APPLICATION — Use Case : Changer le statut d'une commande
 *  Couche  : application/usecase
 * ═══════════════════════════════════════════════════════════
 */
    @Slf4j
    @Service
    @Transactional
    class ChangerStatutCommandeUseCaseImpl implements ChangerStatutCommandeUseCase {

        private final CommandeRepositoryPort commandeRepository
        private final CommandeEventPublisherPort commandeEventPublisher
        private final NotificationEventPublisherPort notificationPublisher
        private final AuditEventPublisherPort auditPublisher

        ChangerStatutCommandeUseCaseImpl(CommandeRepositoryPort commandeRepository,
                                         CommandeEventPublisherPort commandeEventPublisher,
                                         NotificationEventPublisherPort notificationPublisher,
                                         AuditEventPublisherPort auditPublisher) {
            this.commandeRepository     = commandeRepository
            this.commandeEventPublisher = commandeEventPublisher
            this.notificationPublisher  = notificationPublisher
            this.auditPublisher         = auditPublisher
        }

        @Override
        Commande changerStatut(String commandeId, StatutCommande nouveauStatut, String acteurId) {

            String correlationId = UUID.randomUUID().toString()
            log.info("[{}] Changement statut {} → {} par {}",
                    correlationId, commandeId, nouveauStatut, acteurId)

            // 1. Charger l'agrégat (via port → adaptateur PostgreSQL)
            def commande = commandeRepository.trouverParId(commandeId)
                    .orElseThrow { new IllegalArgumentException("Commande introuvable: ${commandeId}") }

            StatutCommande ancienStatut = commande.statut

            // 2. Appliquer la transition (la règle métier est dans l'agrégat)
            //    → lève TransitionStatutInvalideException si illégale
            commande.changerStatut(nouveauStatut)

            // 3. Persister
            def commandeMiseAJour = commandeRepository.sauvegarder(commande)

            // 4. Événement pressing.commande.events
            commandeEventPublisher.publier(new CommandeEventV1(
                    eventType            : CommandeEventV1.EventType.STATUT_CHANGE.name(),
                    correlationId        : correlationId,
                    commandeId           : commandeId,
                    clientId             : commande.clientId,
                    clientEmail          : commande.clientEmail,
                    clientTelephone      : commande.clientTelephone,
                    ancienStatut         : ancienStatut,
                    nouveauStatut        : nouveauStatut,
                    actionParUtilisateur : acteurId
            ))

            // 5. Notification si le client doit être alerté
            if (statutNecessiteNotification(nouveauStatut)) {
                notificationPublisher.publier(construireNotification(
                        commandeMiseAJour, nouveauStatut, correlationId
                ))
            }

            // 6. Audit
            auditPublisher.publier(new AuditEventV1(
                    correlationId  : correlationId,
                    acteurId       : acteurId,
                    acteurType     : 'USER',
                    action         : 'CHANGEMENT_STATUT',
                    entiteType     : 'COMMANDE',
                    entiteId       : commandeId,
                    ancienneValeur : [statut: ancienStatut.name()],
                    nouvelleValeur : [statut: nouveauStatut.name()],
                    resultat       : AuditEventV1.ActionResult.SUCCESS,
                    message        : "${ancienStatut} → ${nouveauStatut}",
                    service        : 'pressing-api'
            ))

            log.info("[{}] Statut changé: {} → {}", correlationId, ancienStatut, nouveauStatut)
            commandeMiseAJour
        }

        // ─── Helpers privés ───────────────────────────────────────────────────────

        private static boolean statutNecessiteNotification(StatutCommande statut) {
            statut in [StatutCommande.PRISE_EN_CHARGE, StatutCommande.PRET, StatutCommande.LIVRE]
        }

        private static NotificationEventV1 construireNotification(Commande commande,
                                                                  StatutCommande statut,
                                                                  String correlationId) {
            def (sujet, corps) = switch (statut) {
                case StatutCommande.PRISE_EN_CHARGE ->
                    ["Commande prise en charge", "Votre commande est en cours de traitement."]
                case StatutCommande.PRET ->
                    ["Votre commande est prête ! 🎉", "Vous pouvez venir récupérer vos articles."]
                case StatutCommande.LIVRE ->
                    ["Commande livrée", "Votre commande a été livrée. Merci de votre confiance !"]
                default ->
                    ["Mise à jour commande", "Votre commande a été mise à jour."]
            }

            new NotificationEventV1(
                    correlationId: correlationId,
                    commandeId: commande.id,
                    canal: NotificationEventV1.CanalNotification.SMS_ET_EMAIL,
                    priorite: NotificationEventV1.PrioriteNotification.HIGH,
                    destinataireEmail: commande.clientEmail,
                    destinataireTelephone: commande.clientTelephone,
                    sujet: sujet,
                    corps: corps,
                    typeEvenementSource: statut.name()
            )

        }
    }