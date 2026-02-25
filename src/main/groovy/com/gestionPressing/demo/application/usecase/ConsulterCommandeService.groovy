package com.gestionPressing.demo.application.usecase

import com.gestionPressing.demo.domain.models.Commande
import com.gestionPressing.demo.domain.ports.input.ConsulterCommandeUseCase;
import com.gestionPressing.demo.domain.ports.output.CommandeRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ConsulterCommandeService implements ConsulterCommandeUseCase {

    private final CommandeRepositoryPort commandeRepository;

    public ConsulterCommandeService(CommandeRepositoryPort commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Commande consulter(String commandeId) {
        return commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    @Override
    public List<Commande> consulterParClient(String clientId) {
        return commandeRepository.findByClientId(clientId);
    }
}