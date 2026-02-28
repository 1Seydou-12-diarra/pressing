package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.*
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

    @Override
    Commande creer(String clientId, String clientEmail, String clientTelephone,
                   String description, Double montantTotal, String agenceId, String employeId) {

        def request = new CreerCommandeRequest(
                clientId: clientId as Long,
                agenceId: agenceId as Long,
                employeId: employeId as Long,
                articles: [],
                dateRetraitPrevue: null
        )

        CommandeResponse response = creerCommande(request)

        return commandeRepository.findById(response.id)
                .orElseThrow { new CommandeNotFoundException(response.id) }
    }

    CommandeResponse creerCommande(CreerCommandeRequest request) {

        def articles = request.articles.collect { req ->
            ArticleCommande.creer(
                    req.typeVetement,
                    req.service,
                    req.tarifUnitaire,
                    req.observations,
                    req.codeBarres
            )
        }

        def commande = Commande.creerDepot(
                request.clientId,
                request.agenceId,
                request.employeId,
                articles,
                request.dateRetraitPrevue
        )

        commande.montantTotal = articles.sum { it.tarifUnitaire ?: 0 } ?: 0

        def saved = commandeRepository.save(commande)

        def historique = HistoriqueStatut.creer(
                saved.id,
                null,
                StatutCommande.DEPOSE,
                request.employeId
        )
        historiqueRepository.save(historique)

        eventPublisher.publierCommandeCreee(saved)

        return toResponse(saved)
    }

    @Override
    Commande changerStatut(String commandeId,
                           StatutCommande nouveauStatut,
                           String employeId) {

        def commande = commandeRepository.findById(commandeId)
                .orElseThrow { new CommandeNotFoundException(commandeId) }

        def ancienStatut = StatutCommande.valueOf(commande.statut)

        commande.changerStatut(nouveauStatut)

        def saved = commandeRepository.save(commande)

        def historique = HistoriqueStatut.creer(
                saved.id,
                ancienStatut,
                nouveauStatut,
                employeId as Long
        )
        historiqueRepository.save(historique)

        eventPublisher.publierStatutChange(saved)

        return saved
    }

    @Override
    Commande consulter(String commandeId) {
        return commandeRepository.findById(commandeId)
                .orElseThrow { new CommandeNotFoundException(commandeId) }
    }

    @Override
    List<Commande> consulterParClient(String clientId) {
        return commandeRepository.findByClientId(clientId as Long)
    }

    private static CommandeResponse toResponse(Commande c) {
        new CommandeResponse(
                id: c.id,
                clientId: c.client.id,
                agenceId: c.agence?.id,
                employeId: c.employe?.id,
                statut: StatutCommande.valueOf(c.statut),
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
                            statut: a.statut ? StatutArticle.valueOf(a.statut) : null
                    )
                }
        )
    }
}