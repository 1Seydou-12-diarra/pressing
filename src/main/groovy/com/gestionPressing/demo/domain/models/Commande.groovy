package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "commande")
@Canonical
@ToString(includeNames = true)
class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client

    @ManyToOne
    @JoinColumn(name = "agence_id")
    Agence agence

    @ManyToOne
    @JoinColumn(name = "employe_id")
    Employe employe

    String statut
    BigDecimal montantTotal
    LocalDateTime dateDepot
    LocalDateTime dateRetraitPrevue

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    List<ArticleCommande> articles = []

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    List<Paiement> paiements = []

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    List<HistoriqueStatut> historiques = []
}
