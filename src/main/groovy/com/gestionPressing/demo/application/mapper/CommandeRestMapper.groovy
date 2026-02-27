package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.ArticleCommandeResponse
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.domain.models.ArticleCommande
import com.gestionPressing.demo.domain.models.Commande
import org.springframework.stereotype.Component

@Component
class CommandeMapper {

    // ─── Domain → JPA Entity ────────────────────────────────

    Commande toEntity(Commande commande) {
        def entity = new Commande(
                id              : commande.id,
                clientId        : commande.clientId,
                agenceId        : commande.agenceId,
                employeId       : commande.employeId,
                statut          : commande.statut,
                montantTotal    : commande.montantTotal,
                dateDepot       : commande.dateDepot,
                dateRetraitPrevue: commande.dateRetraitPrevue
        )
        entity.articles = commande.articles.collect { toArticleEntity(it, entity) }
        return entity
    }

    ArticleCommande toArticleEntity(ArticleCommande article,
                                             Commande commandeEntity) {
        return new ArticleCommande(
                id           : article.id,
                commande     : commandeEntity,
                typeVetement : article.typeVetement,
                service      : article.service,
                tarifUnitaire: article.tarifUnitaire,
                observations : article.observations,
                codeBarres   : article.codeBarres,
                statut       : article.statut
        )
    }

    // ─── JPA Entity → Domain ────────────────────────────────

    Commande toDomain(Commande entity) {
        def articles = entity.articles.collect { toArticleDomain(it, entity.id) }
        return new Commande(
                id               : entity.id,
                clientId         : entity.clientId,
                agenceId         : entity.agenceId,
                employeId        : entity.employeId,
                statut           : entity.statut,
                montantTotal     : entity.montantTotal,
                dateDepot        : entity.dateDepot,
                dateRetraitPrevue: entity.dateRetraitPrevue,
                articles         : articles
        )
    }

    ArticleCommande toArticleDomain(ArticleCommande entity, Long commandeId) {
        return new ArticleCommande(
                id           : entity.id,
                commandeId   : commandeId,
                typeVetement : entity.typeVetement,
                service      : entity.service,
                tarifUnitaire: entity.tarifUnitaire,
                observations : entity.observations,
                codeBarres   : entity.codeBarres,
                statut       : entity.statut
        )
    }

    // ─── Domain → Response DTO ──────────────────────────────

    CommandeResponse toResponse(Commande commande) {
        return new CommandeResponse(
                id               : commande.id,
                clientId         : commande.clientId,
                agenceId         : commande.agenceId,
                employeId        : commande.employeId,
                statut           : commande.statut,
                montantTotal     : commande.montantTotal,
                dateDepot        : commande.dateDepot,
                dateRetraitPrevue: commande.dateRetraitPrevue,
                articles         : commande.articles.collect { toArticleResponse(it) }
        )
    }

    ArticleCommandeResponse toArticleResponse(ArticleCommande article) {
        return new ArticleCommandeResponse(
                id           : article.id,
                typeVetement : article.typeVetement,
                service      : article.service,
                tarifUnitaire: article.tarifUnitaire,
                observations : article.observations,
                codeBarres   : article.codeBarres,
                statut       : article.statut
        )
    }
}