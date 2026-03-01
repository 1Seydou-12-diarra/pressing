package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.ProduitStockRequestDto
import com.gestionPressing.demo.application.dtos.ProduitStockResponseDto
import com.gestionPressing.demo.application.mapper.ProduitStockMapper
import com.gestionPressing.demo.domain.ports.AgenceRepositoryPort
import com.gestionPressing.demo.domain.ports.ProduitStockRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProduitStockService {

    private final ProduitStockRepositoryPort stockRepository

    // je dois reactiver le constructeur de produit
    private final AgenceRepositoryPort agenceRepository

    ProduitStockService(ProduitStockRepositoryPort stockRepository,
                        AgenceRepositoryPort agenceRepository) {
        this.stockRepository = stockRepository
        this.agenceRepository = agenceRepository
    }

    @Transactional
    ProduitStockResponseDto creer(ProduitStockRequestDto dto) {
        def agence = agenceRepository.findById(dto.agenceId)
                .orElseThrow { new RuntimeException("Agence introuvable") }

        def stock = ProduitStockMapper.toEntity(dto, agence)
        stockRepository.save(stock)
        ProduitStockMapper.toDto(stock)
    }

    List<ProduitStockResponseDto> lister() {
        stockRepository.findAll().collect { ProduitStockMapper.toDto(it) }
    }

    @Transactional
    ProduitStockResponseDto modifier(Long id, ProduitStockRequestDto dto) {
        def stock = stockRepository.findById(id)
                .orElseThrow { new RuntimeException("Produit stock introuvable") }

        def agence = agenceRepository.findById(dto.agenceId)
                .orElseThrow { new RuntimeException("Agence introuvable") }

        stock.nom = dto.nom
        stock.quantite = dto.quantite
        stock.seuilAlerte = dto.seuilAlerte
        stock.unite = dto.unite
        stock.agence = agence

        stockRepository.save(stock)
        ProduitStockMapper.toDto(stock)
    }

    @Transactional
    void supprimer(Long id) {
        stockRepository.deleteById(id)
    }

    /**
     * Retourne la liste des produits sous le seuil d’alerte
     */
    List<ProduitStockResponseDto> produitsSeuilBas() {
        stockRepository.findProduitsSeuilBas().collect { ProduitStockMapper.toDto(it) }
    }
}