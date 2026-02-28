
package com.gestionPressing.demo.domain.models

import com.gestionPressing.demo.domain.enums.StatutCommande
import com.gestionPressing.demo.domain.models.Agence
import com.gestionPressing.demo.domain.models.ArticleCommande
import com.gestionPressing.demo.domain.models.Client
import com.gestionPressing.demo.domain.models.Employe
import com.gestionPressing.demo.domain.models.HistoriqueStatut
import com.gestionPressing.demo.domain.models.Paiement
import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

import java.time.LocalDateTime

@Entity
@Table(name = "commande")
@Canonical
@ToString(includeNames = true)

class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    Client client

    @ManyToOne
    @JoinColumn(name = "agence_id")
    Agence agence

    @ManyToOne
    @JoinColumn(name = "employe_id")
    Employe employe

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    StatutCommande statut

    @Column(name = "montant_total", nullable = false)
    BigDecimal montantTotal = 0

    @Column(name = "date_depot", nullable = false, updatable = false)
    LocalDateTime dateDepot = LocalDateTime.now()

    @Column(name = "date_retrait_prevue")
    LocalDateTime dateRetraitPrevue

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ArticleCommande> articles = []

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Paiement> paiements = []

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    List<HistoriqueStatut> historiques = []

    BigDecimal calculerMontantTotal() {
        if (!articles) return 0
        articles.sum { it.tarifUnitaire ?: 0 } ?: 0
    }
}