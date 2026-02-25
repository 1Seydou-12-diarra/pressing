package com.gestionPressing.demo.domain.events


import java.time.Instant

/**
 * DOMAINE — Événement d'audit versionné V1
 * Couche  : domain/events
 * Topic   : pressing.audit.events
 */
class AuditEventV1 {

    final String  eventId      = UUID.randomUUID().toString()
    final String  version      = 'v1'
    final Instant occurredAt   = Instant.now()
    final String  correlationId

    // Qui a fait quoi
    final String       acteurId
    final String       acteurType   // USER, SYSTEM, ADMIN
    final String       acteurIp
    final String       action       // LOGIN, MODIF_TARIF, CHANGEMENT_STATUT...
    final ActionResult resultat

    // Sur quoi
    final String              entiteType
    final String              entiteId
    final Map<String, Object> ancienneValeur
    final Map<String, Object> nouvelleValeur

    // Contexte technique
    final String service
    final String endpoint
    final String traceId
    final String message

    AuditEventV1(Map args) {
        this.correlationId  = args.correlationId
        this.acteurId       = args.acteurId
        this.acteurType     = args.acteurType
        this.acteurIp       = args.acteurIp
        this.action         = args.action
        this.resultat       = args.resultat
        this.entiteType     = args.entiteType
        this.entiteId       = args.entiteId
        this.ancienneValeur = args.ancienneValeur
        this.nouvelleValeur = args.nouvelleValeur
        this.service        = args.service
        this.endpoint       = args.endpoint
        this.traceId        = args.traceId
        this.message        = args.message
    }

    enum ActionResult {
        SUCCESS, FAILURE, PARTIAL
    }
}