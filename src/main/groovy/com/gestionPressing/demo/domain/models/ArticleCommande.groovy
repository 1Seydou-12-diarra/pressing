package com.gestionPressing.demo.domain.models

import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*
import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.models.Commande


@Entity
@Table(name = "article_commande")
@Canonical
@ToString(includeNames = true)
class ArticleCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    Commande commande

    @Column(name = "type_vetement", nullable = false, length = 100)
    String typeVetement

    @Column(nullable = false, length = 100)
    String service

    @Column(name = "tarif_unitaire", nullable = false, columnDefinition = "numeric(10,2)")
    BigDecimal tarifUnitaire

    @Column(columnDefinition = "TEXT")
    String observations

    @Column(name = "code_barres", nullable = false, unique = true, length = 100)
    String codeBarres

    @Column(nullable = false, length = 30)
    String statut

    // ─── Méthode statique pour créer un article (remplace 'creer')
    static ArticleCommande creer(String typeVetement,
                                 String service,
                                 BigDecimal tarifUnitaire,
                                 String observations,
                                 String codeBarres) {
        return new ArticleCommande(
                typeVetement: typeVetement,
                service: service,
                tarifUnitaire: tarifUnitaire,
                observations: observations,
                codeBarres: codeBarres,
                statut: StatutCommande.DEPOSE.name() // statut initial
        )
    }
}