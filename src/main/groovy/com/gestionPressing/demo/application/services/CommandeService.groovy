package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.ArticleCommandeResponse
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.application.dtos.CreerCommandeRequest
import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.exception.CommandeNotFoundException
import com.gestionPressing.demo.domain.models.ArticleCommande
import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.models.HistoriqueStatut
import com.gestionPressing.demo.domain.ports.input.ChangerStatutCommandeUseCase
import com.gestionPressing.demo.domain.ports.input.ConsulterCommandeUseCase
import com.gestionPressing.demo.domain.ports.input.CreerCommandeUseCase
import com.gestionPressing.demo.domain.ports.output.CommandeEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeRepositoryPort
import com.gestionPressing.demo.infrastructure.repositories.HistoriqueStatutRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommandeService implements CreerCommandeUseCase, ChangerStatutCommandeUseCase, ConsulterCommandeUseCase {

    private final CommandeRepositoryPort commandeRepository
    private final HistoriqueStatutRepository historiqueRepository
    private final CommandeEventPublisherPort eventPublisher

    CommandeService(CommandeRepositoryPort commandeRepository,
                    HistoriqueStatutRepository historiqueRepository,
                    CommandeEventPublisherPort eventPublisher) {
        this.commandeRepository = commandeRepository
        this.historiqueRepository = historiqueRepository
        this.eventPublisher = eventPublisher
    }

    // ─────────────────────────────────────────
    // IMPLEMENTATION DE L'INTERFACE CREERCOMMANDEUSECASE
    // ─────────────────────────────────────────
    @Override
    Commande creer(String clientId, String clientEmail, String clientTelephone,
                   String description, Double montantTotal, String agenceId, String employeId) {

        // Construire un CreerCommandeRequest interne pour réutiliser la logique existante
        def request = new CreerCommandeRequest(
                clientId: clientId,
                agenceId: agenceId as Long,
                employeId: employeId as Long,
                articles: [], // si tu veux passer des articles
                dateRetraitPrevue: null
        )

        // Appelle la méthode interne qui contient la logique métier
        CommandeResponse response = creerCommande(request)

        // Retourne l'entité Commande pour respecter la signature de l'interface
        return commandeRepository.findById(response.id)
                .orElseThrow { new CommandeNotFoundException(response.id) }
    }

    // ─────────────────────────────────────────
    // LOGIQUE MÉTIER DE CRÉATION DE COMMANDE
    // ─────────────────────────────────────────
    CommandeResponse creerCommande(CreerCommandeRequest request) {

        // Mapper les articles
        def articles = request.articles.collect { req ->
            ArticleCommande.creer(
                    req.typeVetement,
                    req.service,
                    req.tarifUnitaire,
                    req.observations,
                    req.codeBarres
            )
        }

        // Créer l’agrégat commande
        def commande = Commande.creerDepot(
                request.clientId,
                request.agenceId,
                request.employeId,
                articles,
                request.dateRetraitPrevue
        )

        // Calcul automatique du montant
        commande.montantTotal = articles.sum { it.tarifUnitaire ?: 0 } ?: 0

        // Persister
        def saved = commandeRepository.save(commande)

        // Historique initial
        def historique = HistoriqueStatut.creer(
                saved.id,
                null,
                StatutCommande.DEPOSE,
                request.employeId
        )
        historiqueRepository.save(historique)

        // Événement Kafka
        eventPublisher.publierCommandeCreee(saved)

        return toResponse(saved)
    }

    // ─────────────────────────────────────────
    // CHANGER LE STATUT
    // ─────────────────────────────────────────
    @Override
    Commande changerStatut(String commandeId,
                           StatutCommande nouveauStatut,
                           String employeId) {

        def commande = commandeRepository.findById(commandeId)
                .orElseThrow { new CommandeNotFoundException(commandeId) }

        def ancienStatut = commande.statut

        // Validation workflow
        commande.changerStatut(nouveauStatut)

        // Persister
        def saved = commandeRepository.save(commande)

        // Historique
        def historique = HistoriqueStatut.creer(
                saved.id,
                ancienStatut,
                nouveauStatut,
                employeId
        )
        historiqueRepository.save(historique)

        eventPublisher.publierStatutChange(saved)

        return saved
    }

    // ─────────────────────────────────────────
    // IMPLEMENTATION DE L'INTERFACE CONSULTERCOMMANDEUSECASE
    // ─────────────────────────────────────────
    @Override
    Commande consulter(String commandeId) {
        return commandeRepository.findById(commandeId)
                .orElseThrow { new CommandeNotFoundException(commandeId) }
    }

    @Override
    List<Commande> consulterParClient(String clientId) {
        return commandeRepository.findByClientId(clientId)
    }

    // ─────────────────────────────────────────
    // MAPPER ENTITY → DTO
    // ─────────────────────────────────────────
    private static CommandeResponse toResponse(Commande c) {
        return new CommandeResponse(
                id: c.id,
                clientId: c.clientId,
                agenceId: c.agenceId,
                employeId: c.employeId,
                statut: c.statut?.name(),
                montantTotal: c.montantTotal,
                dateDepot: c.dateDepot,
                dateRetraitPrevue: c.dateRetraitPrevue,
                articles: c.articles.collect { a ->
                    new ArticleCommandeResponse(
                            id: a.id,
                            typeVetement: a.typeVetement,
                            service: a.service,
                            tarifUnitaire: a.tarifUnitaire,
                            observations: a.observations,
                            codeBarres: a.codeBarres,
                            statut: a.statut?.name()
                    )
                }
        )
    }
}