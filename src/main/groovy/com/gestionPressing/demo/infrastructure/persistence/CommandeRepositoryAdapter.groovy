package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.ArticleCommande
import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.ports.output.CommandeRepository
import com.gestionPressing.demo.infrastructure.repositories.CommandeJpaRepository
import com.gestionPressing.demo.domain.enums.StatutCommande
import org.springframework.stereotype.Component

@Component
class CommandeRepositoryAdapter implements CommandeRepository {

    private final CommandeJpaRepository jpaRepository

    CommandeRepositoryAdapter(CommandeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository
    }

    @Override
    Commande save(Commande commande) {
        jpaRepository.save(commande).tap { saved ->
            saved.articles.each { it.commande = saved }
        }
    }

    @Override
    Optional<Commande> findById(Long id) {
        jpaRepository.findById(id).map { toDomain(it) }
    }

    @Override
    List<Commande> findAll() {
        jpaRepository.findAll().collect { toDomain(it) }
    }

    @Override
    List<Commande> findByClientId(Long clientId) {
        jpaRepository.findByClientId(clientId).collect { toDomain(it) }
    }

    // --- Mapping JPA → Domain ---

    private Commande toDomain(Commande jpa) {
        new Commande(
                id:                jpa.id,
                client:            jpa.client,
                agence:            jpa.agence,
                employe:           jpa.employe,
                statut:            jpa.statut,
                montantTotal:      jpa.montantTotal,
                dateDepot:         jpa.dateDepot,
                dateRetraitPrevue: jpa.dateRetraitPrevue,
                articles:          jpa.articles.collect { toArticleDomain(it) }
        )
    }

    private ArticleCommande toArticleDomain(ArticleCommande jpa) {
        new ArticleCommande(
                id:            jpa.id,
                commande:      jpa.commande,
                typeVetement:  jpa.typeVetement,
                service:       jpa.service,
                tarifUnitaire: jpa.tarifUnitaire,
                observations:  jpa.observations,
                codeBarres:    jpa.codeBarres,
                statut:        jpa.statut
        )
    }
}