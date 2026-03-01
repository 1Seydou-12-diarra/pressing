package com.gestionPressing.demo.domain.ports

import com.gestionPressing.demo.domain.models.Agence


interface AgenceRepositoryPort {

    Agence save(Agence agence)

    List<Agence> findAll()

    Optional<Agence> findById(Long id)

    Optional<Agence> findByCodeAgence(String code)

    void deleteById(Long id)
}