package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "historique_statut")
@Canonical
@ToString(includeNames = true)
class HistoriqueStatut {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id

        @ManyToOne
        @JoinColumn(name = "commande_id", nullable = false)
        Commande commande

        @Column(name = "statut_precedent", length = 30)
        String statutPrecedent

        @Column(name = "statut_nouveau", nullable = false, length = 30)
        String statutNouveau

        @ManyToOne
        @JoinColumn(name = "employe_id")
        Employe employe

        @Column(name = "date_changement")
        LocalDateTime dateChangement = LocalDateTime.now()
    }

