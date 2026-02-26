package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.ports.output.CommandeRepositoryPort
import com.gestionPressing.demo.infrastructure.repositories.CommandeJpaRepository
import org.springframework.stereotype.Component

@Component
class CommandeJpaAdapter implements CommandeRepositoryPort {

    private final CommandeJpaRepository repository

    CommandeJpaAdapter(CommandeJpaRepository repository) {
        this.repository = repository
    }

    @Override
    Commande sauvegarder(Commande commande) {
        def entity = toEntity(commande)
        def saved = repository.save(entity)
        return toDomain(saved)
    }

    @Override
    Optional<Commande> trouverParId(String commandeId) {
        repository.findById(commandeId.toLong()).map { toDomain(it) }
    }

    @Override
    List<Commande> trouverParClientId(String clientId) {
        repository.findByClientId(clientId.toLong())
                .collect { toDomain(it) }
    }

    @Override
    boolean existeParId(String commandeId) {
        repository.existsById(commandeId.toLong())
    }

    // 🔁 Mapping

    private static Commande toEntity(Commande c) {
        new Commande(
                id: c.id,
                clientId: c.clientId,
                statut: c.statut,
                montantTotal: c.montantTotal
        )
    }

    private static Commande toDomain(Commande e) {
        new Commande(
                id: e.id,
                clientId: e.clientId,
                statut: e.statut,
                montantTotal: e.montantTotal
        )
    }
}