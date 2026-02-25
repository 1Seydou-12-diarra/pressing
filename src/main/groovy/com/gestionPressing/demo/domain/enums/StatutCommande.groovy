package com.gestionPressing.demo.domain.enums


/**
 * ═══════════════════════════════════════════════════════════
 *  DOMAINE — Enum StatutCommande
 *  Couche : domain/model
 *
 *  Valeur métier pure. Aucune dépendance externe.
 *  Utilisé par : Commande, CommandeEventV1, UseCases...
 * ═══════════════════════════════════════════════════════════
 */
enum StatutCommande {
    EN_ATTENTE,
    PRISE_EN_CHARGE,
    EN_COURS_LAVAGE,
    EN_COURS_REPASSAGE,
    PRET,
    LIVRE,
    ANNULE
}
