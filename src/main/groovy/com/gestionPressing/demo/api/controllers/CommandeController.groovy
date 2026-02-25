package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.ChangerStatutRequest
import com.gestionPressing.demo.application.dtos.CommandeRequest
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.application.mapper.CommandeRestMapper
import com.gestionPressing.demo.domain.ports.input.ChangerStatutCommandeUseCase
import com.gestionPressing.demo.domain.ports.input.ConsulterCommandeUseCase
import com.gestionPressing.demo.domain.ports.input.CreerCommandeUseCase
import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * ═══════════════════════════════════════════════════════════
 *  EXPOSITION — Adaptateur d'ENTRÉE REST
 *  Couche : exposition/rest/controller
 *
 *  Appelle UNIQUEMENT les ports d'entrée (interfaces).
 *  Ne connaît pas les use cases concrets.
 *  Ne fait PAS de logique métier.
 * ═══════════════════════════════════════════════════════════
 */
@Slf4j
@RestController
@RequestMapping('/api/v1/commandes')
class CommandeController {

    // Injection par INTERFACES (ports d'entrée), pas par implémentations
    private final CreerCommandeUseCase creerCommandeUseCase
    private final ChangerStatutCommandeUseCase changerStatutUseCase
    private final ConsulterCommandeUseCase consulterCommandeUseCase
    private final CommandeRestMapper mapper

    CommandeController(CreerCommandeUseCase creerCommandeUseCase,
                       ChangerStatutCommandeUseCase changerStatutUseCase,
                       ConsulterCommandeUseCase consulterCommandeUseCase,
                       CommandeRestMapper mapper) {
        this.creerCommandeUseCase   = creerCommandeUseCase
        this.changerStatutUseCase   = changerStatutUseCase
        this.consulterCommandeUseCase = consulterCommandeUseCase
        this.mapper                 = mapper
    }

    @PostMapping
    ResponseEntity<CommandeResponse> creer(@RequestBody CommandeRequest request,
                                           @RequestHeader('X-User-Id') String acteurId) {
        log.info("POST /commandes - client: {}", request.clientId)
        def commande = creerCommandeUseCase.creer(
                request.clientId, request.clientEmail, request.clientTelephone,
                request.description, request.montantTotal, acteurId
        )
        ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(commande))
    }

    @PatchMapping('/{commandeId}/statut')
    ResponseEntity<CommandeResponse> changerStatut(@PathVariable String commandeId,
                                                   @RequestBody ChangerStatutRequest request,
                                                   @RequestHeader('X-User-Id') String acteurId) {
        log.info("PATCH /commandes/{}/statut → {}", commandeId, request.nouveauStatut)
        def commande = changerStatutUseCase.changerStatut(
                commandeId, StatutCommande.valueOf(request.nouveauStatut), acteurId
        )
        ResponseEntity.ok(mapper.toResponse(commande))
    }

    @GetMapping('/{commandeId}')
    ResponseEntity<CommandeResponse> consulter(@PathVariable String commandeId) {
        def commande = consulterCommandeUseCase.consulter(commandeId)
        ResponseEntity.ok(mapper.toResponse(commande))
    }

    @GetMapping('/client/{clientId}')
    ResponseEntity<List<CommandeResponse>> consulterParClient(@PathVariable String clientId) {
        def commandes = consulterCommandeUseCase.consulterParClient(clientId)
        ResponseEntity.ok(commandes.collect { mapper.toResponse(it) })
    }
}