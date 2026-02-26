package com.gestionPressing.demo.infrastructure.persistence

import com.gestionPressing.demo.application.mapper.ClientMapper
import com.gestionPressing.demo.domain.models.Client
import com.gestionPressing.demo.domain.ports.output.ClientRepository
import com.gestionPressing.demo.domain.ports.output.ClientRepository
import com.gestionPressing.demo.infrastructure.repositories.ClientJpaRepository
import org.springframework.stereotype.Component

@Component
class ClientRepositoryAdapter implements ClientRepository {



        private final ClientJpaRepository jpa

        ClientRepositoryAdapter(ClientJpaRepository jpa) {
            this.jpa = jpa
        }

        Client save(Client client) {
            def entity = new Client(
                    id: client.id,
                    nom: client.nom,
                    prenom: client.prenom,
                    telephone: client.telephone,
                    email: client.email,
                    adresse: client.adresse,
                    pointsFidelite: client.pointsFidelite,
                    actif: client.actif
            )
            def saved = jpa.save(entity)
            toDomain(saved)
        }

        Optional<Client> findById(Long id) {
            jpa.findById(id).map(this::toDomain)
        }

        List<Client> search(String nom, String telephone) {
            jpa.findAll().collect { toDomain(it) }
        }

        private Client toDomain(Client e) {
            new Client(
                    id: e.id,
                    nom: e.nom,
                    prenom: e.prenom,
                    telephone: e.telephone,
                    email: e.email,
                    adresse: e.adresse,
                    pointsFidelite: e.pointsFidelite,
                    actif: e.actif
            )
        }
    }
