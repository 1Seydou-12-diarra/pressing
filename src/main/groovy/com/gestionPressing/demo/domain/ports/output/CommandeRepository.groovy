package com.gestionPressing.demo.domain.ports.output

import com.gestionPressing.demo.domain.models.Commande

interface CommandeRepository {
    Commande save(Commande commande)
    Optional<Commande> findById(Long id)
    List<Commande> findAll()
    List<Commande> findByClientId(Long clientId)
}