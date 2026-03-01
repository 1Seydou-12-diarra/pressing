package com.gestionPressing.demo.infrastructure.repositories

import com.gestionPressing.demo.domain.models.ProduitStock
import org.springframework.data.jpa.repository.*
import org.springframework.stereotype.Repository

@Repository
interface ProduitStockJpaRepository extends JpaRepository<ProduitStock, Long> {

    @Query("SELECT p FROM ProduitStock p WHERE p.quantite <= p.seuilAlerte")
    List<ProduitStock> findProduitsSeuilBas()
}