package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.domain.models.Client
import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.ports.input.ClientUseCase
import com.gestionPressing.demo.domain.ports.output.ClientRepository
import org.springframework.stereotype.Service

@Service
class ClientService implements ClientUseCase {

    private final ClientRepository repository

    ClientService(ClientRepository repository) {
        this.repository = repository
    }

    Client creer(Client client) {
        repository.save(client)
    }

    Client modifier(Long id, Client client) {
        def existant = repository.findById(id)
                .orElseThrow { new RuntimeException("Client introuvable") }

        existant.nom = client.nom
        existant.prenom = client.prenom
        existant.email = client.email
        existant.adresse = client.adresse

        repository.save(existant)
    }

    Client trouverParId(Long id) {
        repository.findById(id)
                .orElseThrow { new RuntimeException("Client introuvable") }
    }

    List<Client> rechercher(String nom, String telephone) {
        repository.search(nom, telephone)
    }

    void desactiver(Long id) {
        def client = trouverParId(id)
        client.actif = false
        repository.save(client)
    }
    List<Commande> historiqueCommandes(Long clientId) {
        repository.findById(clientId)
                .orElseThrow { new RuntimeException("Client introuvable") }

        commandeRepository.findByClientId(clientId)
    }

    void crediterPoints(Long clientId, BigDecimal montant) {
        def client = trouverParId(clientId)
        int points = montant.intValue() / 1000
        client.ajouterPoints(points)
        repository.save(client)
    }

    void utiliserPoints(Long clientId, int points) {
        def client = trouverParId(clientId)
        client.utiliserPoints(points)
        repository.save(client)
    }
}