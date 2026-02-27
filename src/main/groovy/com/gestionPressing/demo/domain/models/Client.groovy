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

        @Column(nullable = false, length = 100)
        String nom

        @Column(length = 100)
        String prenom

        @Column(nullable = false, length = 30, unique = true)
        String telephone

        @Column(length = 150)
        String email

        @Column(columnDefinition = "TEXT")
        String adresse

        @Column(name = "points_fidelite", columnDefinition = "int default 0")
        Integer pointsFidelite = 0

        @Column(columnDefinition = "boolean default true")
        Boolean actif = true

        @Column(name = "date_creation")
        LocalDateTime dateCreation = LocalDateTime.now()

        @OneToMany(mappedBy = "client")
        List<Commande> commandes

    void ajouterPoints(int points) {
        this.pointsFidelite += points
    }

    void utiliserPoints(int points) {
        if (points > pointsFidelite) {
            throw new IllegalStateException("Points insuffisants")
        }
        this.pointsFidelite -= points
    }
    }



