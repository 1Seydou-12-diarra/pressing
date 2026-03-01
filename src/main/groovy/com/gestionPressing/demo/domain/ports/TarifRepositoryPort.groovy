package com.gestionPressing.demo.domain.ports

import com.gestionPressing.demo.domain.models.Tarif
import java.time.LocalDate

interface TarifRepositoryPort {
    Optional<Tarif> findByTypeVetementAndTypeService(String typeVetement, String typeService)
    Optional<Tarif> findTarifActif(String typeVetement, String typeService, LocalDate date)
    Tarif save(Tarif tarif)
    List<Tarif> findAll()
    Optional<Tarif> findById(Long id)
    void deleteById(Long id)
}