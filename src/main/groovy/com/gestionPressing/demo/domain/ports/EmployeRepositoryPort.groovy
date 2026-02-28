package com.gestionPressing.demo.domain.ports

import com.gestionPressing.demo.domain.models.Employe


interface EmployeRepositoryPort {

    Employe save(Employe employe)

    List<Employe> findAll()

    Optional<Employe> findById(Long id)

    void deleteById(Long id)

    Optional<Employe> findByKeycloakId(String keycloakId)

    /**
     * Retourne les employés par agence
     */
    List<Employe> findByAgenceId(Long agenceId)
}