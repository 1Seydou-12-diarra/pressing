package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

@Entity
@Table(name = "employe")
@Canonical
@ToString(includeNames = true)
class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String keycloakId
    String nom
    String prenom
    String role
    Boolean actif

    @ManyToOne
    @JoinColumn(name = "agence_id")
    Agence agence

    @OneToMany(mappedBy = "employe")
    List<Commande> commandes = []

    @OneToMany(mappedBy = "employe")
    List<HistoriqueStatut> historiques = []
}