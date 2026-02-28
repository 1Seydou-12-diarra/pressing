package com.gestionPressing.demo.domain.models

import jakarta.persistence.*
import groovy.transform.Canonical
import java.math.BigDecimal

@Entity
@Table(name = "article_commande")
@Canonical
class ArticleCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    Commande commande

    @Column(name = "type_vetement", nullable = false)
    String typeVetement

    @Column(nullable = false)
    String service

    @Column(name = "tarif_unitaire", nullable = false)
    BigDecimal tarifUnitaire

    @Column
    String observations

    @Column(name = "code_barres", nullable = false, unique = true)
    String codeBarres

    @Column(nullable = false)
    String statut = "DEPOSE"

    static ArticleCommande creer(String typeVetement, String service, BigDecimal tarif, String observations, String codeBarres) {
        new ArticleCommande(
                typeVetement: typeVetement,
                service: service,
                tarifUnitaire: tarif,
                observations: observations,
                codeBarres: codeBarres,
                statut: "DEPOSE"
        )
    }
}