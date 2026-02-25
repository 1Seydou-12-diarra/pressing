package com.gestionPressing.demo.domain.exception

import com.gestionPressing.demo.domain.enums.StatutCommande


/**
 * DOMAINE — Exception métier (transition illégale)
 * Couche : domain/exception
 */
class TransitionStatutInvalideException extends RuntimeException {

    final String commandeId
    final StatutCommande ancienStatut
    final StatutCommande nouveauStatut

    TransitionStatutInvalideException(String commandeId,
                                      StatutCommande ancienStatut,
                                      StatutCommande nouveauStatut) {
        super("Transition interdite pour commande [${commandeId}] : ${ancienStatut} → ${nouveauStatut}")
        this.commandeId   = commandeId
        this.ancienStatut = ancienStatut
        this.nouveauStatut = nouveauStatut
    }
}