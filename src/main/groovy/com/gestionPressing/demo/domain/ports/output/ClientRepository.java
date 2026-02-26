package com.gestionPressing.demo.domain.ports.output;


import com.gestionPressing.demo.domain.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Long id);
    List<Client> search(String nom, String telephone);
}