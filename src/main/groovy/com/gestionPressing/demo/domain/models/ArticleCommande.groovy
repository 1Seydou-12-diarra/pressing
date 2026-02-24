package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

@Entity
@Table(name = "article_commande")
@Canonical
@ToString(includeNames = true)
class ArticleCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "commande_id")
    Commande commande

    String typeVetement
    String service
    BigDecimal tarifUnitaire
    String observations
    String codeBarres
    String statut
}