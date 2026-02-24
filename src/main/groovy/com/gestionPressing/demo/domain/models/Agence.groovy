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

    String nom
    String adresse
    String telephone
    String codeAgence
    Boolean actif

    @OneToMany(mappedBy = "agence")
    List<Employe> employes = []

    @OneToMany(mappedBy = "agence")
    List<Commande> commandes = []

    @OneToMany(mappedBy = "agence")
    List<ProduitStock> produitsStock = []
}
