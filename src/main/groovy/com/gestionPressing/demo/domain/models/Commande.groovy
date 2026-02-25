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
        @JoinColumn(name = "client_id", nullable = false)
        Client client

        @ManyToOne
        @JoinColumn(name = "agence_id")
        Agence agence

        @ManyToOne
        @JoinColumn(name = "employe_id")
        Employe employe

        @Column(nullable = false, length = 30)
        String statut

        @Column(name = "montant_total", columnDefinition = "numeric(12,2) default 0")
        BigDecimal montantTotal = 0

        @Column(name = "date_depot")
        LocalDateTime dateDepot = LocalDateTime.now()

        @Column(name = "date_retrait_prevue")
        LocalDateTime dateRetraitPrevue

        @OneToMany(mappedBy = "commande")
        List<ArticleCommande> articles

        @OneToMany(mappedBy = "commande")
        List<Paiement> paiements

        @OneToMany(mappedBy = "commande")
        List<HistoriqueStatut> historiques
    }

