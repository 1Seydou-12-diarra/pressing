package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.TarifRequestDto
import com.gestionPressing.demo.application.dtos.TarifResponseDto
import com.gestionPressing.demo.application.mapper.TarifMapper
import com.gestionPressing.demo.domain.ports.TarifRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class TarifService {

    private final TarifRepositoryPort repository

    TarifService(TarifRepositoryPort repository) {
        this.repository = repository
    }

    @Transactional
    TarifResponseDto creer(TarifRequestDto dto) {

        if (dto.prix <= 0) {
            throw new RuntimeException("Le prix doit être positif")
        }

        def tarif = TarifMapper.toEntity(dto)
        repository.save(tarif)

        return TarifMapper.toDto(tarif)
    }

    List<TarifResponseDto> lister() {
        repository.findAll().collect { TarifMapper.toDto(it) }
    }

    @Transactional
    TarifResponseDto modifier(Long id, TarifRequestDto dto) {

        def tarif = repository.findById(id)
                .orElseThrow { new RuntimeException("Tarif introuvable") }

        tarif.typeVetement = dto.typeVetement
        tarif.typeService = dto.typeService
        tarif.prix = dto.prix
        tarif.actif = dto.actif
        tarif.dateDebut = dto.dateDebut
        tarif.dateFin = dto.dateFin

        repository.save(tarif)

        return TarifMapper.toDto(tarif)
    }

    @Transactional
    void supprimer(Long id) {
        repository.deleteById(id)
    }

    /**
     * Retourne le tarif actif (promotion incluse)
     */
    BigDecimal obtenirTarif(String typeVetement, String typeService) {

        def aujourdHui = LocalDate.now()

        def tarif = repository.findTarifActif(typeVetement, typeService, aujourdHui)
                .orElseThrow { new RuntimeException("Aucun tarif actif trouvé") }

        return tarif.prix
    }
}