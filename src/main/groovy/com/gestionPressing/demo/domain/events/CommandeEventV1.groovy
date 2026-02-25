package com.gestionPressing.demo.domain.events


import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.gestionPressing.demo.domain.enums.StatutCommande

import java.time.Instant

/**
 * ═══════════════════════════════════════════════════════════
 *  DOMAINE — Événement de commande versionné V1
 *  Couche  : domain/events
 *  Topic   : pressing.commande.events
 *
 *  Les événements sont des VALUE OBJECTS immuables.
 *  Ils appartiennent au domaine, pas à l'infrastructure.
 * ═══════════════════════════════════════════════════════════
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = 'eventType')
class CommandeEventV1 {

    // ─── Métadonnées (versioning + tracing) ───────────────────────────────────
    final String  eventId       = UUID.randomUUID().toString()
    final String  version       = 'v1'
    final Instant occurredAt    = Instant.now()
    final String  eventType     // valeur de l'enum EventType
    final String  correlationId // propagé depuis la requête HTTP (MDC)

    // ─── Données métier ───────────────────────────────────────────────────────
    final String        commandeId
    final String        clientId
    final String        clientEmail
    final String        clientTelephone
    final StatutCommande ancienStatut
    final StatutCommande nouveauStatut
    final String        actionParUtilisateur
    final Double        montantTotal

    // ─── Constructeur nommé (style Groovy) ────────────────────────────────────
    CommandeEventV1(Map args) {
        this.eventType            = args.eventType
        this.correlationId        = args.correlationId
        this.commandeId           = args.commandeId
        this.clientId             = args.clientId
        this.clientEmail          = args.clientEmail
        this.clientTelephone      = args.clientTelephone
        this.ancienStatut         = args.ancienStatut
        this.nouveauStatut        = args.nouveauStatut
        this.actionParUtilisateur = args.actionParUtilisateur
        this.montantTotal         = args.montantTotal
    }

    enum EventType {
        COMMANDE_CREEE,
        STATUT_CHANGE,
        COMMANDE_ANNULEE,
        COMMANDE_MODIFIEE
    }
}