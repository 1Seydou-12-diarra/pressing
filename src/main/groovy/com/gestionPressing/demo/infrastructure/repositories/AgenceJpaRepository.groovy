package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.Agence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AgenceJpaRepository extends JpaRepository<Agence, Long> {

    Optional<Agence> findByCodeAgence(String codeAgence)
}
