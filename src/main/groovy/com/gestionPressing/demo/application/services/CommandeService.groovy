package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.ArticleCommandeResponse
import com.gestionPressing.demo.application.dtos.CommandeResponse
import com.gestionPressing.demo.application.dtos.CreerCommandeRequest
import com.gestionPressing.demo.application.usecase.ChangerStatutCommandeUseCaseImpl
import com.gestionPressing.demo.application.usecase.ConsulterCommandeService
import com.gestionPressing.demo.application.usecase.CreerCommandeUseCaseImpl
import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.exception.CommandeNotFoundException
import com.gestionPressing.demo.domain.models.ArticleCommande
import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.models.HistoriqueStatut
import com.gestionPressing.demo.domain.ports.output.AuditEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeEventPublisherPort
import com.gestionPressing.demo.domain.ports.output.CommandeRepositoryPort
import com.gestionPressing.demo.domain.ports.output.NotificationEventPublisherPort
import com.gestionPressing.demo.infrastructure.repositories.CommandeJpaRepository
import com.gestionPressing.demo.infrastructure.repositories.HistoriqueStatutRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommandeService extends CreerCommandeUseCaseImpl, ChangerStatutCommandeUseCaseImpl, ConsulterCommandeService {

    private final CommandeJpaRepository commandeRepository
    private final HistoriqueStatutRepository historiqueRepository
    private final CommandeEventPublisherPort eventPublisher

    CommandeService(CommandeRepositoryPort commandeRepository, CommandeEventPublisherPort commandeEventPublisher, NotificationEventPublisherPort notificationPublisher, AuditEventPublisherPort auditPublisher, CommandeJpaRepository commandeRepository1, HistoriqueStatutRepository historiqueRepository, CommandeEventPublisherPort eventPublisher) {
        super(commandeRepository, commandeEventPublisher, notificationPublisher, auditPublisher)
        this.commandeRepository = commandeRepository1
        this.historiqueRepository = historiqueRepository
        this.eventPublisher = eventPublisher
    }
// ─────────────────────────────────────────
    // CRÉATION D’UNE COMMANDE (DÉPÔT)
    // ─────────────────────────────────────────

    @Override
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

        // ✅ CALCUL AUTOMATIQUE DU MONTANT
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

    // ─────────────────────────────────────────
    // CHANGEMENT DE STATUT
    // ─────────────────────────────────────────

    @Override
    CommandeResponse changerStatut(Long commandeId,
                                   StatutCommande nouveauStatut,
                                   Long employeId) {

        def commande = commandeRepository.findById(commandeId)
                .orElseThrow { new CommandeNotFoundException(commandeId) }

        def ancienStatut = commande.statut

        commande.changerStatut(nouveauStatut)

        def saved = commandeRepository.save(commande)

        def historique = HistoriqueStatut.creer(
                commandeId,
                ancienStatut,
                nouveauStatut,
                employeId
        )

        historiqueRepository.save(historique)

        eventPublisher.publierStatutChange(saved)

        return toResponse(saved)
    }

    // ─────────────────────────────────────────
    // CONSULTATION
    // ─────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    CommandeResponse getById(Long id) {
        def commande = commandeRepository.findById(id)
                .orElseThrow { new CommandeNotFoundException(id) }
        return toResponse(commande)
    }

    @Override
    @Transactional(readOnly = true)
    List<CommandeResponse> getByClient(Long clientId) {
        commandeRepository.findByClientId(clientId)
                .collect { toResponse(it) }
    }

    @Override
    @Transactional(readOnly = true)
    List<CommandeResponse> getByStatut(StatutCommande statut) {
        commandeRepository.findByStatut(statut)
                .collect { toResponse(it) }
    }

    @Override
    @Transactional(readOnly = true)
    List<CommandeResponse> getByAgence(Long agenceId) {
        commandeRepository.findByAgenceId(agenceId)
                .collect { toResponse(it) }
    }

    // ─────────────────────────────────────────
    // MAPPER ENTITY → DTO
    // ─────────────────────────────────────────

    private static CommandeResponse toResponse(Commande c) {
        new CommandeResponse(
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