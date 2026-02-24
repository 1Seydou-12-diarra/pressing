package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "tarif")
@Canonical
@ToString(includeNames = true)
class Tarif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String typeVetement
    String typeService
    BigDecimal prix
    Boolean actif
    LocalDateTime dateDebut
    LocalDateTime dateFin
}
