package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.CommandeRequest
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.application.mapper.CommandeMapper
import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.ports.AgenceRepositoryPort
import com.gestionPressing.demo.domain.ports.EmployeRepositoryPort
import com.gestionPressing.demo.domain.ports.output.ClientRepository
import com.gestionPressing.demo.domain.ports.output.CommandeRepository
import com.gestionPressing.demo.domain.ports.TarifRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime

@Service
@Transactional
class CommandeService {

    private final CommandeRepository commandeRepository
    private final TarifRepositoryPort tarifRepository
    private final CommandeMapper mapper
    private final ClientRepository clientRepository
    private final AgenceRepositoryPort agenceRepositoryPort
    private final EmployeRepositoryPort employeRepositoryPort

    CommandeService(CommandeRepository commandeRepository, TarifRepositoryPort tarifRepository, CommandeMapper mapper, ClientRepository clientRepository, AgenceRepositoryPort agenceRepositoryPort, EmployeRepositoryPort employeRepositoryPort) {
        this.commandeRepository = commandeRepository
        this.tarifRepository = tarifRepository
        this.mapper = mapper
        this.clientRepository = clientRepository
        this.agenceRepositoryPort = agenceRepositoryPort
        this.employeRepositoryPort = employeRepositoryPort
    }

    CommandeResponse creerDepot(CommandeRequest request) {
        def client = clientRepository.findById(request.clientId)
                .orElseThrow { new RuntimeException("Client introuvable") }
        def agence = agenceRepositoryPort.findById(request.agenceId)
                .orElseThrow { new RuntimeException("Agence introuvable") }
        def employe = employeRepositoryPort.findById(request.employeId)
                .orElseThrow { new RuntimeException("Employé introuvable") }

        def commande = new Commande(
                client:            client,
                agence:            agence,
                employe:           employe,
                statut:            StatutCommande.DEPOSE,  // ✅ corrigé
                dateDepot:         LocalDateTime.now(),
                dateRetraitPrevue: request.dateRetraitPrevue
        )

        commande.articles = request.articles.collect { articleReq ->

            def article = mapper.toArticleDomain(articleReq)

            def tarif = tarifRepository
                    .findByTypeVetementAndTypeService(articleReq.typeVetement, articleReq.service)
                    .orElseThrow {
                        new RuntimeException(
                                "Tarif introuvable pour ${articleReq.typeVetement} / ${articleReq.service}"
                        )
                    }

            article.tarifUnitaire = tarif.prix

            // 🔥 LA LIGNE MANQUANTE
            article.commande = commande

            article
        }

        commande.montantTotal = commande.calculerMontantTotal()

        def saved = commandeRepository.save(commande)
        mapper.toResponse(saved)
    }

    CommandeResponse changerStatut(Long commandeId, String nouveauStatutStr) {
        def commande = commandeRepository.findById(commandeId)
                .orElseThrow { new RuntimeException("Commande ${commandeId} introuvable") }
        def nouveauStatut = StatutCommande.valueOf(nouveauStatutStr)
        commande.changerStatut(nouveauStatut)
        def saved = commandeRepository.save(commande)
        mapper.toResponse(saved)
    }

    CommandeResponse getCommande(Long id) {
        def commande = commandeRepository.findById(id)
                .orElseThrow { new RuntimeException("Commande ${id} introuvable") }
        mapper.toResponse(commande)
    }

    List<CommandeResponse> listerCommandes() {
        commandeRepository.findAll().collect { mapper.toResponse(it) }
    }

    Commande getCommandeDomain(Long id) {
        commandeRepository.findById(id)
                .orElseThrow { new RuntimeException("Commande ${id} introuvable") }
    }
}