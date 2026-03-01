package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.HistoriqueStatut
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {

}