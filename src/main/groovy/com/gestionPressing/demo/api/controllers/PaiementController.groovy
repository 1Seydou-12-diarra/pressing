package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.PaiementRequestDto
import com.gestionPressing.demo.application.dtos.PaiementResponseDto
import com.gestionPressing.demo.application.services.PaiementService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * API REST Paiement
 * Sécurisée avec contrôle d'accès fin
 */
@RestController
@RequestMapping("/api/paiements")
class PaiementController {

    private final PaiementService service

    PaiementController(PaiementService service) {
        this.service = service
    }

    /**
     * Enregistrer un paiement
     * Accessible uniquement aux CAISSIERS et ADMIN
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CAISSIER')")
    @PostMapping
    PaiementResponseDto payer(@RequestBody PaiementRequestDto dto) {
        service.enregistrerPaiement(dto)
    }

    /**
     * Remboursement partiel
     * Accessible uniquement aux ADMIN et MANAGER
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/remboursement/{commandeId}")
    PaiementResponseDto rembourser(
            @PathVariable Long commandeId,
            @RequestParam BigDecimal montant
    ) {
        service.rembourser(commandeId, montant)
    }

    /**
     * Consulter le total payé d'une commande
     * Accessible à ADMIN, MANAGER et CAISSIER
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CAISSIER',ROLE_CAISIER)")
    @GetMapping("/total/{commandeId}")
    BigDecimal total(@PathVariable Long commandeId) {
        service.totalPaye(commandeId)
    }
}