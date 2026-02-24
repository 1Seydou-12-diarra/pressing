package com.gestionPressing.demo.domain.models

import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "client")
@Canonical
@ToString(includeNames = true)
class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String nom
    String prenom
    String telephone
    String email
    String adresse
    Integer pointsFidelite
    Boolean actif
    LocalDateTime dateCreation

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    List<Commande> commandes = []
}
