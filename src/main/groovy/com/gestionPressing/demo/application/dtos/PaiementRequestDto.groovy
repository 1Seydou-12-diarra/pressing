package com.gestionPressing.demo.application.dtos

import com.gestionPressing.demo.domain.enums.ModePaiement

import java.math.BigDecimal

/**
 * DTO utilisé pour enregistrer un paiement
 */
class PaiementRequestDto {

    Long commandeId
    BigDecimal montant
    ModePaiement modePaiement
    String reference
}