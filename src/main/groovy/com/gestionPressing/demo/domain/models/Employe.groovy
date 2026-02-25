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

        @Column(name = "keycloak_id", nullable = false, unique = true, length = 100)
        String keycloakId

        @Column(nullable = false, length = 100)
        String nom

        @Column(nullable = false, length = 100)
        String prenom

        @Column(nullable = false, length = 50)
        String role

        @ManyToOne
        @JoinColumn(name = "agence_id")
        Agence agence

        @Column(columnDefinition = "boolean default true")
        Boolean actif = true

        @OneToMany(mappedBy = "employe")
        List<Commande> commandes

        @OneToMany(mappedBy = "employe")
        List<HistoriqueStatut> historiques
    }




