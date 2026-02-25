package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.domain.models.Commande
import org.springframework.stereotype.Component

/**
 * EXPOSITION — Mapper REST ↔ Domaine
 * Couche : exposition/rest/mapper
 *
 * Isole les DTOs HTTP du modèle domaine.
 */
@Component
class CommandeRestMapper {

    CommandeResponse toResponse(Commande commande) {
        new CommandeResponse(
                id           : commande.id,
                clientId     : commande.clientId,
                statut       : commande.statut.name(),
                description  : commande.description,
                montantTotal : commande.montantTotal,
                createdAt    : commande.createdAt?.toString(),
                updatedAt    : commande.updatedAt?.toString()
        )
    }
}