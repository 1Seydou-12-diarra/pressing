package com.gestionPressing.demo.application.dtos


/**
 * EXPOSITION — DTOs REST (Request / Response)
 * Couche : exposition/rest/dto
 *
 * Ces objets ne traversent JAMAIS le domaine.
 * Le mapper les convertit en/depuis les objets domaine.
 */

// ─── Requêtes ─────────────────────────────────────────────────────────────────
class CommandeRequest {
    String clientId
    String clientEmail
    String clientTelephone
    String description
    Double montantTotal
}

class ChangerStatutRequest {
    String nouveauStatut   // "PRISE_EN_CHARGE", "PRET", etc.
}

// ─── Réponses ─────────────────────────────────────────────────────────────────
class CommandeResponse {
    String id
    String clientId
    String statut
    String description
    Double montantTotal
    String createdAt
    String updatedAt
}