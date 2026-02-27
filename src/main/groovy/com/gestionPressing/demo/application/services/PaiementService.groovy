package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.PaiementRequestDto
import com.gestionPressing.demo.application.dtos.PaiementResponseDto
import com.gestionPressing.demo.domain.models.Paiement
import com.gestionPressing.demo.domain.ports.PaiementRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service métier Paiement
 */
@Service
class PaiementService {

    private final PaiementRepositoryPort paiementRepository

    PaiementService(PaiementRepositoryPort paiementRepository) {
        this.paiementRepository = paiementRepository
    }

    /**
     * Enregistrer un paiement normal
     */
    @Transactional
    PaiementResponseDto enregistrerPaiement(PaiementRequestDto dto) {

        if (dto.montant <= 0) {
            throw new RuntimeException("Le montant doit être supérieur à 0")
        }

        Paiement paiement = PaiementMapper.toEntity(dto)

        paiement = paiementRepository.save(paiement)

        return PaiementMapper.toDto(paiement)
    }

    /**
     * Remboursement partiel
     */
    @Transactional
    PaiementResponseDto rembourser(Long commandeId, BigDecimal montant) {

        if (montant <= 0) {
            throw new RuntimeException("Montant invalide")
        }

        Paiement remboursement = new Paiement(
                commandeId: commandeId,
                montant: montant.negate(),
                remboursement: true
        )

        remboursement = paiementRepository.save(remboursement)

        return PaiementMapper.toDto(remboursement)
    }

    /**
     * Calculer le total payé
     */
    BigDecimal totalPaye(Long commandeId) {
        paiementRepository.totalPayeParCommande(commandeId)
    }
}