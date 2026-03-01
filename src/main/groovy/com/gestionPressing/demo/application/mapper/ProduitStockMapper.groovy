package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.ProduitStockRequestDto
import com.gestionPressing.demo.application.dtos.ProduitStockResponseDto
import com.gestionPressing.demo.domain.models.ProduitStock


class ProduitStockMapper {

    static ProduitStock toEntity(ProduitStockRequestDto dto, def agence) {
        new ProduitStock(
                agence: agence,
                nom: dto.nom,
                quantite: dto.quantite,
                seuilAlerte: dto.seuilAlerte,
                unite: dto.unite
        )
    }

    static ProduitStockResponseDto toDto(ProduitStock stock) {
        new ProduitStockResponseDto(
                id: stock.id,
                agenceId: stock.agence?.id,
                nom: stock.nom,
                quantite: stock.quantite,
                seuilAlerte: stock.seuilAlerte,
                unite: stock.unite
        )
    }
}