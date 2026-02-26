package com.gestionPressing.demo.domain.ports.input

import com.gestionPressing.demo.domain.models.Client


interface ClientUseCase {
    Client creer(Client client)
    Client modifier(Long id, Client client)
    void desactiver(Long id)
    Client trouverParId(Long id)
    List<Client> rechercher(String nom, String telephone)
}