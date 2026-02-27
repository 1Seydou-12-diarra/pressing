package com.gestionPressing.demo.application.dtos

import com.gestionPressing.demo.domain.enums.ModePaiement

import java.math.BigDecimal
import java.time.LocalDateTime

class PaiementResponseDto {

    Long id
    Long commandeId
    BigDecimal montant
    ModePaiement modePaiement
    LocalDateTime datePaiement
    Boolean remboursement
}