package com.gestionPressing.demo.domain.ports.output


import com.gestionPressing.demo.domain.models.Commande

interface CommandeRepositoryPort {
    Commande save(Commande commande)
    Optional<Commande> findById(Long id)
    List<Commande> findByClientId(Long clientId)
}