package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.ArticleRequest
import com.gestionPressing.demo.application.dtos.ArticleResponse
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.domain.models.ArticleCommande
import com.gestionPressing.demo.domain.models.Commande
import org.springframework.stereotype.Component

@Component
class CommandeMapper {

    // Domain → Response DTO
    CommandeResponse toResponse(Commande commande) {
        new CommandeResponse(
                id:                commande.id,
                clientId:  commande.client?.id,
                agenceId:  commande.agence?.id,
                employeId: commande.employe?.id,
                statut:            commande.statut.name(),
                montantTotal:      commande.montantTotal,
                dateDepot:         commande.dateDepot,
                dateRetraitPrevue: commande.dateRetraitPrevue,
                articles:          commande.articles.collect { toArticleResponse(it) }
        )
    }

    ArticleResponse toArticleResponse(ArticleCommande article) {
        new ArticleResponse(
                id:            article.id,
                typeVetement:  article.typeVetement,
                service:       article.service,
                tarifUnitaire: article.tarifUnitaire,
                observations:  article.observations,
                codeBarres:     article.codeBarres,
                statut:        article.statut
        )
    }

    // Request DTO → Domain (sans tarif, le service s'en charge)
    ArticleCommande toArticleDomain(ArticleRequest req) {
        new ArticleCommande(
                typeVetement: req.typeVetement,
                service:      req.service,
                observations: req.observations,
                codeBarres:    req.codeBarres,
                statut:       "DEPOSE"
        )
    }
}
