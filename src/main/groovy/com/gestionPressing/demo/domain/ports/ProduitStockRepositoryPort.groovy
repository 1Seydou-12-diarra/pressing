package com.gestionPressing.demo.domain.ports

import com.gestionPressing.demo.domain.models.ProduitStock


interface ProduitStockRepositoryPort {

    ProduitStock save(ProduitStock stock)

    List<ProduitStock> findAll()

    Optional<ProduitStock> findById(Long id)

    void deleteById(Long id)

    /**
     * Retourne les produits dont la quantité est inférieure au seuil
     */
    List<ProduitStock> findProduitsSeuilBas()
}