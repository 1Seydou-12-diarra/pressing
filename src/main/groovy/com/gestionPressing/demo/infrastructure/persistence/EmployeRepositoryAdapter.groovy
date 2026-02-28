package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.Employe
import com.gestionPressing.demo.domain.ports.EmployeRepositoryPort
import com.gestionPressing.demo.infrastructure.repositories.EmployeJpaRepository
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class EmployeRepositoryAdapter implements EmployeRepositoryPort {

    private final EmployeJpaRepository repository

    EmployeRepositoryAdapter(EmployeJpaRepository repository) {
        this.repository = repository
    }

    @Override
    Employe save(Employe employe) { repository.save(employe) }

    @Override
    List<Employe> findAll() { repository.findAll() }

    @Override
    Optional<Employe> findById(Long id) { repository.findById(id) }

    @Override
    void deleteById(Long id) { repository.deleteById(id) }

    @Override
    Optional<Employe> findByKeycloakId(String keycloakId) {
        repository.findByKeycloakId(keycloakId)
    }

    @Override
    List<Employe> findByAgenceId(Long agenceId) {
        repository.findByAgenceId(agenceId)
    }
}
