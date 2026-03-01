package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.PaiementRequestDto
import com.gestionPressing.demo.application.dtos.PaiementResponseDto
import com.gestionPressing.demo.domain.models.Paiement


/**
 * Mapper Paiement <-> DTO
 */
class PaiementMapper {

    static Paiement toEntity(PaiementRequestDto dto) {
        new Paiement(
                commandeId: dto.commandeId,
                montant: dto.montant,
                modePaiement: dto.modePaiement,
                reference: dto.reference
        )
    }

    static PaiementResponseDto toDto(Paiement paiement) {
        new PaiementResponseDto(
                id: paiement.id,
                commandeId: paiement.commandeId,
                montant: paiement.montant,
                modePaiement: paiement.modePaiement,
                datePaiement: paiement.datePaiement,
                remboursement: paiement.remboursement
        )
    }
}
