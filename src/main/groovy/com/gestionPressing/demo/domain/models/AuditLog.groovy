package com.gestionPressing.demo.domain.models


import groovy.transform.Canonical
import groovy.transform.ToString
import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "audit_log")
@Canonical
/*

@Canonical permet de créer les constructeur automatiquement
    */

@ToString(includeNames = true)
class AuditLog {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id

        @Column(nullable = false, length = 100)
        String action

        @Column(nullable = false, length = 100)
        String entite

        @Column(name = "entite_id")
        Long entiteId

        @Column(length = 150)
        String utilisateur

        @Column(name = "details_json", columnDefinition = "jsonb")
        String detailsJson

        @Column(name = "timestamp")
        LocalDateTime timestamp = LocalDateTime.now()
    }


