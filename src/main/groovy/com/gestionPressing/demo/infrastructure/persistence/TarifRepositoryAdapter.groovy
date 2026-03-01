package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.Tarif
import com.gestionPressing.demo.domain.ports.TarifRepositoryPort


// ← changer l'import
import com.gestionPressing.demo.infrastructure.repositories.TarifJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class TarifRepositoryAdapter implements TarifRepositoryPort {             // ← changer ici

    private final TarifJpaRepository jpaRepository

    TarifRepositoryAdapter(TarifJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository
    }

    @Override
    Optional<Tarif> findByTypeVetementAndTypeService(String typeVetement, String typeService) {
        jpaRepository
                .findByTypeVetementAndTypeServiceAndActifTrue(typeVetement, typeService)
                .map { toTarifDomain(it) }
    }

    // ── Méthodes supplémentaires utilisées par TarifService ──────────────

    @Override
    Tarif save(Tarif tarif) {
        // mapper domain → JPA puis sauvegarder
        def saved = jpaRepository.save(toJpa(tarif))
        toTarifDomain(saved)
    }

    @Override
    List<Tarif> findAll() {
        jpaRepository.findAll().collect { toTarifDomain(it) }
    }

    @Override
    Optional<Tarif> findById(Long id) {
        jpaRepository.findById(id).map { toTarifDomain(it) }
    }

    @Override
    void deleteById(Long id) {
        jpaRepository.deleteById(id)
    }

    @Override
    Optional<Tarif> findTarifActif(String typeVetement, String typeService, LocalDate date) {
        jpaRepository
                .findTarifActif(typeVetement, typeService, date)
                .map { toTarifDomain(it) }
    }

    // ── Mappers privés ────────────────────────────────────────────────────

    private Tarif toTarifDomain(def jpa) {
        new Tarif(
                id:           jpa.id,
                typeVetement: jpa.typeVetement,
                typeService:  jpa.typeService,
                prix:         jpa.prix,
                actif:        jpa.actif,
                dateDebut:    jpa.dateDebut,
                dateFin:      jpa.dateFin
        )
    }

    private def toJpa(Tarif tarif) {
        // retourne l'entité JPA (même classe Tarif si tu n'as pas de TarifJpa séparé)
        tarif
    }
}