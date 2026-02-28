package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.domain.models.ProduitStock
import com.gestionPressing.demo.domain.ports.ProduitStockRepositoryPort
import com.gestionPressing.demo.infrastructure.repositories.ProduitStockJpaRepository
import org.springframework.stereotype.Component
import java.util.List
import java.util.Optional

@Component
class ProduitStockRepositoryAdapter implements ProduitStockRepositoryPort {

    private final ProduitStockJpaRepository repository

    ProduitStockRepositoryAdapter(ProduitStockJpaRepository repository) {
        this.repository = repository
    }

    @Override
    ProduitStock save(ProduitStock stock) { repository.save(stock) }

    @Override
    List<ProduitStock> findAll() { repository.findAll() }

    @Override
    Optional<ProduitStock> findById(Long id) { repository.findById(id) }

    @Override
    void deleteById(Long id) { repository.deleteById(id) }

    @Override
    List<ProduitStock> findProduitsSeuilBas() { repository.findProduitsSeuilBas() }
}
