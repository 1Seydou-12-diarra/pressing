package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

@Entity
@Table(name = "agence")
@Canonical
@ToString(includeNames = true)

    class Agence {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id

        @Column(nullable = false, length = 150)
        String nom

        @Column(columnDefinition = "TEXT")
        String adresse

        @Column(length = 30)
        String telephone

        @Column(name = "code_agence", nullable = false, unique = true, length = 50)
        String codeAgence

        @Column(columnDefinition = "boolean default true")
        Boolean actif = true

        @OneToMany(mappedBy = "agence")
        List<Employe> employes

        @OneToMany(mappedBy = "agence")
        List<Commande> commandes

        @OneToMany(mappedBy = "agence")
        List<ProduitStock> stocks
    }




