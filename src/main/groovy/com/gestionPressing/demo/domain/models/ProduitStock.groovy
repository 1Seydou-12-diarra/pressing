package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

@Entity
@Table(name = "produit_stock")
@Canonical
@ToString(includeNames = true)
class ProduitStock {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id

        @ManyToOne
        @JoinColumn(name = "agence_id", nullable = false)
        Agence agence

        @Column(nullable = false, length = 150)
        String nom

        @Column(nullable = false)
        Integer quantite

        @Column(name = "seuil_alerte", nullable = false)
        Integer seuilAlerte

        @Column(nullable = false, length = 30)
        String unite
    }
