package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.Commande
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface CommandeJpaRepository extends JpaRepository<Commande, Long> {

    Commande save(Commande commande)
    Optional<Commande> findById(String id)
    List<Commande> findByClientId(String clientId)

}

