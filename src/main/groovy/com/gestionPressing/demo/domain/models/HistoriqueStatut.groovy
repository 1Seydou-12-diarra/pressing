package com.gestionPressing.demo.domain.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "historique_statut")
class HistoriqueStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    Commande commande

    @Column(name = "statut_precedent")
    String statutPrecedent

    @Column(name = "statut_nouveau", nullable = false)
    String statutNouveau

    @Column(name = "employe_id")
    Long employeId

    @Column(name = "date_changement")
    LocalDateTime dateChangement = LocalDateTime.now()

    static HistoriqueStatut creer(Commande commande, String statutPrecedent, String statutNouveau, Long employeId) {
        new HistoriqueStatut(
                commande: commande,
                statutPrecedent: statutPrecedent,
                statutNouveau: statutNouveau,
                employeId: employeId
        )
    }
}