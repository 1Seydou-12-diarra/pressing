package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.Employe
import org.springframework.data.jpa.repository.*
import org.springframework.stereotype.Repository

@Repository
interface EmployeJpaRepository extends JpaRepository<Employe, Long> {

    Optional<Employe> findByKeycloakId(String keycloakId)

    List<Employe> findByAgenceId(Long agenceId)
}