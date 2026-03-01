package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "tarif")
@Canonical
@ToString(includeNames = true)
class Tarif {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id

        @Column(name = "type_vetement", nullable = false, length = 100)
        String typeVetement

        @Column(name = "type_service", nullable = false, length = 100)
        String typeService

        @Column(nullable = false, columnDefinition = "numeric(10,2)")
        BigDecimal prix

        @Column(columnDefinition = "boolean default true")
        Boolean actif = true

        @Column(name = "date_debut")
        LocalDate dateDebut

        @Column(name = "date_fin")
        LocalDate dateFin
    }

