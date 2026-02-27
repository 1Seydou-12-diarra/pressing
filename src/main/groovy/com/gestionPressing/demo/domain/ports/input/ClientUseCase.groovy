package com.gestionPressing.demo.domain.ports.input

import com.gestionPressing.demo.domain.models.Client
import com.gestionPressing.demo.domain.models.Commande


interface ClientUseCase {
    Client creer(Client client)
    Client modifier(Long id, Client client)
    void desactiver(Long id)
    Client trouverParId(Long id)
    List<Client> rechercher(String nom, String telephone)
    List<Commande> historiqueCommandes(Long clientId)
    void crediterPoints(Long clientId, BigDecimal montant)
    void utiliserPoints(Long clientId, int points)
}