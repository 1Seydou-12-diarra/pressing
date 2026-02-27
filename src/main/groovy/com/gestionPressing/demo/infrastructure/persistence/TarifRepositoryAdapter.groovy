package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.Tarif
import com.gestionPressing.demo.domain.ports.TarifRepositoryPort
import com.gestionPressing.demo.infrastructure.repositories.TarifJpaRepository
import org.springframework.stereotype.Component

import java.time.LocalDate

@Component
    class TarifRepositoryAdapter implements TarifRepositoryPort {

        private final TarifJpaRepository repository

        TarifRepositoryAdapter(TarifJpaRepository repository) {
            this.repository = repository
        }

        @Override
        Tarif save(Tarif tarif) {
            repository.save(tarif)
        }

        @Override
        List<Tarif> findAll() {
            repository.findAll()
        }

        @Override
        Optional<Tarif> findById(Long id) {
            repository.findById(id)
        }

        @Override
        void deleteById(Long id) {
            repository.deleteById(id)
        }


    @Override
        Optional<Tarif> findTarifActif(String typeVetement, String typeService, LocalDate date) {
            // ⚡ IMPORTANT : on appelle la méthode du repository Spring Data
            repository.findTarifActif(typeVetement, typeService, date)
        }
    }
