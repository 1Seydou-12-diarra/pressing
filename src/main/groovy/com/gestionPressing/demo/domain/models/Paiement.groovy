package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "paiement")
@Canonical
@ToString(includeNames = true)
class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "commande_id")
    Commande commande

    BigDecimal montant
    String modePaiement
    LocalDateTime datePaiement
    String reference
}
