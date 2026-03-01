package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.Paiement
import com.gestionPressing.demo.domain.ports.PaiementRepositoryPort
import com.gestionPressing.demo.infrastructure.repositories.PaiementJpaRepository
import org.springframework.stereotype.Component

/**
 * Adapter JPA vers le port métier
 */
@Component
class PaiementRepositoryAdapter implements PaiementRepositoryPort {

    private final PaiementJpaRepository repository

    PaiementRepositoryAdapter(PaiementJpaRepository repository) {
        this.repository = repository
    }

    @Override
    Paiement save(Paiement paiement) {
        repository.save(paiement)
    }

    @Override
    List<Paiement> findByCommandeId(Long commandeId) {
        repository.findByCommandeId(commandeId)
    }

    @Override
    BigDecimal totalPayeParCommande(Long commandeId) {
        repository.totalPayeParCommande(commandeId)
    }
}
