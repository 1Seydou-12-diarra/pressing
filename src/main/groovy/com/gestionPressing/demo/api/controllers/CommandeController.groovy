package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.CommandeRequest
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.application.services.CommandeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/commandes")
class CommandeController {

    private final CommandeService commandeService

    CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService
    }

    // POST /api/commandes → créer un dépôt
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CAISSIER')")
    ResponseEntity<CommandeResponse> creerDepot(@RequestBody CommandeRequest request) {
        def response = commandeService.creerDepot(request)
        ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // GET /api/commandes → lister toutes les commandes
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    ResponseEntity<List<CommandeResponse>> listerCommandes() {
        ResponseEntity.ok(commandeService.listerCommandes())
    }

    // GET /api/commandes/{id} → récupérer une commande
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<CommandeResponse> getCommande(@PathVariable Long id) {
        ResponseEntity.ok(commandeService.getCommande(id))
    }

    // PATCH /api/commandes/{id}/statut → changer le statut
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    ResponseEntity<CommandeResponse> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        ResponseEntity.ok(commandeService.changerStatut(id, statut))
    }
}