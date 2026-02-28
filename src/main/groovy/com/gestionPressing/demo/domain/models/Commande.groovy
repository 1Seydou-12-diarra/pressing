package com.gestionPressing.demo.domain.models

import com.gestionPressing.demo.domain.enums.StatutCommande
import jakarta.persistence.*
import groovy.transform.Canonical
import groovy.transform.ToString
import java.time.LocalDateTime

@Entity
@Table(name = "commande")
@Canonical
@ToString(includeNames = true)
class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    Client client

    @ManyToOne
    @JoinColumn(name = "agence_id")
    Agence agence

    @ManyToOne
    @JoinColumn(name = "employe_id")
    Employe employe

    @Column(nullable = false, length = 30)
    String statut = "DEPOSE"

    @Column(name = "montant_total", columnDefinition = "numeric(12,2) default 0")
    BigDecimal montantTotal = 0

    @Column(name = "date_depot")
    LocalDateTime dateDepot = LocalDateTime.now()

    @Column(name = "date_retrait_prevue")
    LocalDateTime dateRetraitPrevue

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    List<ArticleCommande> articles = []

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    List<HistoriqueStatut> historiques = []

    void changerStatut(StatutCommande nouveauStatut) {
        this.statut = nouveauStatut.name()
    }

    BigDecimal calculerMontantTotal() {
        articles.sum { it.tarifUnitaire ?: 0 } ?: 0
    }

    static Commande creerDepot(Client client, Agence agence, Employe employe, List<ArticleCommande> articles, LocalDateTime dateRetrait) {
        def cmd = new Commande(client: client, agence: agence, employe: employe, dateRetraitPrevue: dateRetrait)
        cmd.articles = articles
        articles.each { it.commande = cmd }
        cmd.montantTotal = cmd.calculerMontantTotal()
        return cmd
    }
}