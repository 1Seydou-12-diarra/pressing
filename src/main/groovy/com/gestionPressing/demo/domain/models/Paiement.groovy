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
        @JoinColumn(name = "commande_id", nullable = false)
        Commande commande

        @Column(nullable = false, columnDefinition = "numeric(12,2)")
        BigDecimal montant

        @Column(name = "mode_paiement", nullable = false, length = 50)
        String modePaiement

        @Column(name = "date_paiement")
        LocalDateTime datePaiement = LocalDateTime.now()

        @Column(length = 100)
        String reference
    }

