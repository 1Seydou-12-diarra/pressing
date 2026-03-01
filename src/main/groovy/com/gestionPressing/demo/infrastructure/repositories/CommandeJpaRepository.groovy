package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.Commande
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommandeJpaRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId)
}