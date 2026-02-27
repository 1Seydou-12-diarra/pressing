package com.gestionPressing.demo.domain.ports;

import com.gestionPressing.demo.domain.models.Tarif;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TarifRepositoryPort {

    Tarif save(Tarif tarif);

    List<Tarif> findAll();

    Optional<Tarif> findById(Long id);

    void deleteById(Long id);

    /**
     * Retourne le tarif actif à une date donnée
     */
    Optional<Tarif> findTarifActif(String typeVetement, String typeService, LocalDate date);
}
