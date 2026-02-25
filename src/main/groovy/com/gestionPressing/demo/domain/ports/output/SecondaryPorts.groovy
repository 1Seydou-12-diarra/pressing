package com.gestionPressing.demo.domain.ports.output

import com.gestionPressing.demo.domain.events.AuditEventV1
import com.gestionPressing.demo.domain.events.CommandeEventV1
import com.gestionPressing.demo.domain.events.NotificationEventV1
import com.gestionPressing.demo.domain.models.Commande


/**
 * ═══════════════════════════════════════════════════════════
 *  DOMAINE — Ports de SORTIE (Secondary/Driven Ports)
 *  Couche  : domain/ports/output
 *
 *  Le DOMAINE définit ce dont il a besoin.
 *  L'INFRASTRUCTURE l'implémente (Kafka, PostgreSQL, Redis...).
 *
 *  Règle : ces interfaces NE référencent que des types du domaine.
 *          Jamais d'import Spring, JPA, Kafka ici.
 * ═══════════════════════════════════════════════════════════
 */

// ─── Port Persistance ─────────────────────────────────────────────────────────
interface CommandeRepositoryPort {
    Commande sauvegarder(Commande commande)
    Optional<Commande> trouverParId(String commandeId)
    List<Commande> trouverParClientId(String clientId)
    boolean existeParId(String commandeId)
}

// ─── Ports Publication Événements ─────────────────────────────────────────────
interface CommandeEventPublisherPort {
    void publier(CommandeEventV1 event)
    void publierSync(CommandeEventV1 event)   // attend l'ACK broker
}

interface NotificationEventPublisherPort {
    void publier(NotificationEventV1 event)
}

interface AuditEventPublisherPort {
    void publier(AuditEventV1 event)
}

// ─── Port Idempotence (cache distribué) ───────────────────────────────────────
interface IdempotencePort {
    boolean dejaTraite(String eventId)
    void marquerCommeTraite(String eventId)
}
