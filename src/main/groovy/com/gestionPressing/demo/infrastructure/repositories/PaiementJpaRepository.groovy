package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.Paiement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PaiementJpaRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByCommandeId(Long commandeId)

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.commande.id = :commandeId")
    Double totalPayeParCommande(@Param("commandeId") Long commandeId);
}