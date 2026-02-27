package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.Tarif
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.time.LocalDate
import java.util.Optional
@Repository
interface TarifJpaRepository extends JpaRepository<Tarif, Long> {

    @Query("""
        SELECT t FROM Tarif t
        WHERE t.typeVetement = :typeVetement
        AND t.typeService = :typeService
        AND t.actif = true
        AND (:date IS NULL OR (t.dateDebut IS NULL OR t.dateDebut <= :date))
        AND (:date IS NULL OR (t.dateFin IS NULL OR t.dateFin >= :date))
        ORDER BY t.dateDebut DESC
    """)
    Optional<Tarif> findTarifActif(@Param("typeVetement") String typeVetement,
                                   @Param("typeService") String typeService,
                                   @Param("date") LocalDate date)
}