package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.AgenceRequestDto
import com.gestionPressing.demo.application.dtos.AgenceResponseDto
import com.gestionPressing.demo.application.services.AgenceService
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/agences")
class AgenceController {

    private final AgenceService service

    AgenceController(AgenceService service) {
        this.service = service
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping
    AgenceResponseDto creer(@RequestBody AgenceRequestDto dto) {
        service.creer(dto)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_EMPLOYE')")
    @GetMapping
    List<AgenceResponseDto> lister() {
        service.lister()
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/{id}")
    AgenceResponseDto modifier(@PathVariable Long id,
                               @RequestBody AgenceRequestDto dto) {
        service.modifier(id, dto)
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    void supprimer(@PathVariable Long id) {
        service.supprimer(id)
    }

    /**
     * 🔥 Transfert de commande
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/transferer-commande/{commandeId}/vers/{agenceId}")
    void transfererCommande(@PathVariable Long commandeId,
                            @PathVariable Long agenceId) {
        service.transfererCommande(commandeId, agenceId)
    }
}