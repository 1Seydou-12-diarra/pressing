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
    @JoinColumn(name = "agence_id")
    Agence agence

    String nom
    Integer quantite
    Integer seuilAlerte
    String unite
}