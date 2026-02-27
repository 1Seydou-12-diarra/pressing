package com.gestionPressing.demo.domain.ports

import com.gestionPressing.demo.domain.models.Paiement


/**
 * Port de persistance des paiements
 */
interface PaiementRepositoryPort {

    Paiement save(Paiement paiement)

    List<Paiement> findByCommandeId(Long commandeId)

    BigDecimal totalPayeParCommande(Long commandeId)
}