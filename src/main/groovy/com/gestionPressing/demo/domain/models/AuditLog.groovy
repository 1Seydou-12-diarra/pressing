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

    String action
    String entite
    Long entiteId
    String utilisateur
    String detailsJson
    LocalDateTime timestamp

}
