package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.Agence
import com.gestionPressing.demo.domain.ports.AgenceRepositoryPort
import com.gestionPressing.demo.infrastructure.repositories.AgenceJpaRepository
import org.springframework.stereotype.Component

@Component
class AgenceRepositoryAdapter implements AgenceRepositoryPort {

    private final AgenceJpaRepository repository

    AgenceRepositoryAdapter(AgenceJpaRepository repository) {
        this.repository = repository
    }

    @Override
    Agence save(Agence agence) { repository.save(agence) }

    @Override
    List<Agence> findAll() { repository.findAll() }

    @Override
    Optional<Agence> findById(Long id) { repository.findById(id) }

    @Override
    Optional<Agence> findByCodeAgence(String code) {
        repository.findByCodeAgence(code)
    }

    @Override
    void deleteById(Long id) { repository.deleteById(id) }
}
