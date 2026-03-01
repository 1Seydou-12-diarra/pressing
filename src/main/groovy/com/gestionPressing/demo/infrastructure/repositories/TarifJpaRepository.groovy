package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.Tarif
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.time.LocalDate

@Repository
interface TarifJpaRepository extends JpaRepository<Tarif, Long> {
    Optional<Tarif> findByTypeVetementAndTypeServiceAndActifTrue(String typeVetement, String typeService)

    @Query("SELECT t FROM Tarif t WHERE t.typeVetement = :typeVetement AND t.typeService = :typeService AND t.actif = true AND (t.dateFin IS NULL OR t.dateFin >= :date)")
    Optional<Tarif> findTarifActif(
            @Param("typeVetement") String typeVetement,
            @Param("typeService") String typeService,
            @Param("date") LocalDate date
    )
}