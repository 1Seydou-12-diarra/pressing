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
    @JoinColumn(name = "commande_id")
    Commande commande

    @ManyToOne
    @JoinColumn(name = "employe_id")
    Employe employe

    String statutPrecedent
    String statutNouveau
    LocalDateTime dateChangement
}
