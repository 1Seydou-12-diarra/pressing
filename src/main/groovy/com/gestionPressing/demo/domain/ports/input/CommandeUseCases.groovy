package com.gestionPressing.demo.domain.ports.input

import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.models.Commande


/**
 * ═══════════════════════════════════════════════════════════
 *  DOMAINE — Ports d'ENTRÉE (Use Cases)
 *  Couche  : domain/ports/input
 *
 *  Ce sont les interfaces que la couche EXPOSITION appelle.
 *  Implémentées dans application/usecase.
 *
 *  Règle : l'exposition NE CONNAÎT PAS les use cases concrets,
 *          seulement ces interfaces.
 * ═══════════════════════════════════════════════════════════
 */

// ─── Use Case 1 : Créer une commande ──────────────────────────────────────────
interface CreerCommandeUseCase {
    Commande creer(String clientId,
                   String clientEmail,
                   String clientTelephone,
                   String description,
                   Double montantTotal,
                   String acteurId)
}

// ─── Use Case 2 : Changer le statut d'une commande ───────────────────────────
interface ChangerStatutCommandeUseCase {
    Commande changerStatut(String commandeId,
                           StatutCommande nouveauStatut,
                           String acteurId)
}

// ─── Use Case 3 : Consulter une commande ─────────────────────────────────────
interface ConsulterCommandeUseCase {
    Commande consulter(String commandeId)
    List<Commande> consulterParClient(String clientId)
}