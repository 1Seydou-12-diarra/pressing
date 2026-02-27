package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.ChangerStatutRequest
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.application.dtos.CreerCommandeRequest
import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.ports.input.ChangerStatutCommandeUseCase
import com.gestionPressing.demo.domain.ports.input.ConsulterCommandeUseCase
import com.gestionPressing.demo.domain.ports.input.CreerCommandeUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/commandes")
class CommandeController {

    private final CreerCommandeUseCase creerCommandeUseCase
    private final ChangerStatutCommandeUseCase changerStatutUseCase
    private final ConsulterCommandeUseCase consulterUseCase

    CommandeController(CreerCommandeUseCase creerCommandeUseCase,
                       ChangerStatutCommandeUseCase changerStatutUseCase,
                       ConsulterCommandeUseCase consulterUseCase) {
        this.creerCommandeUseCase = creerCommandeUseCase
        this.changerStatutUseCase = changerStatutUseCase
        this.consulterUseCase     = consulterUseCase
    }

    // ─── CRÉER UN DÉPÔT ─────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CAISSIER')")
    ResponseEntity<CommandeResponse> creerCommande(
            @Valid @RequestBody CreerCommandeRequest request) {
        def response = creerCommandeUseCase.creerCommande(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ─── CHANGER LE STATUT ───────────────────────────────────

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    ResponseEntity<CommandeResponse> changerStatut(
            @PathVariable Long id,
            @Valid @RequestBody ChangerStatutRequest request) {
        def response = changerStatutUseCase.changerStatut(id, request.nouveauStatut, request.employeId)
        return ResponseEntity.ok(response)
    }

    // ─── CONSULTATION ────────────────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<CommandeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(consulterUseCase.getById(id))
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<List<CommandeResponse>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(consulterUseCase.getByClient(clientId))
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    ResponseEntity<List<CommandeResponse>> getByStatut(@PathVariable StatutCommande statut) {
        return ResponseEntity.ok(consulterUseCase.getByStatut(statut))
    }

    @GetMapping("/agence/{agenceId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    ResponseEntity<List<CommandeResponse>> getByAgence(@PathVariable Long agenceId) {
        return ResponseEntity.ok(consulterUseCase.getByAgence(agenceId))
    }
}