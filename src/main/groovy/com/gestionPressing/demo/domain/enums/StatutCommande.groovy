package com.gestionPressing.demo.domain.enums

enum StatutCommande {
    DEPOSE,
    EN_TRAITEMENT,
    PRET,
    RETIRE

    // Définit les transitions autorisées
    boolean peutPasserA(StatutCommande cible) {
        switch (this) {
            case DEPOSE:       return cible == EN_TRAITEMENT
            case EN_TRAITEMENT: return cible == PRET
            case PRET:         return cible == RETIRE
            default:           return false
        }
    }
}