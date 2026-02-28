package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.AgenceRequestDto
import com.gestionPressing.demo.application.dtos.AgenceResponseDto
import com.gestionPressing.demo.application.mapper.AgenceMapper
import com.gestionPressing.demo.domain.ports.AgenceRepositoryPort
import com.gestionPressing.demo.domain.ports.output.CommandeRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AgenceService {

    private final AgenceRepositoryPort agenceRepository
    private final CommandeRepositoryPort commandeRepository

    AgenceService(AgenceRepositoryPort agenceRepository,
                  CommandeRepositoryPort commandeRepository) {
        this.agenceRepository = agenceRepository
        this.commandeRepository = commandeRepository
    }

    @Transactional
    AgenceResponseDto creer(AgenceRequestDto dto) {
        def agence = AgenceMapper.toEntity(dto)
        agenceRepository.save(agence)
        AgenceMapper.toDto(agence)
    }

    List<AgenceResponseDto> lister() {
        agenceRepository.findAll().collect { AgenceMapper.toDto(it) }
    }

    @Transactional
    AgenceResponseDto modifier(Long id, AgenceRequestDto dto) {
        def agence = agenceRepository.findById(id)
                .orElseThrow { new RuntimeException("Agence introuvable") }

        agence.nom = dto.nom
        agence.adresse = dto.adresse
        agence.telephone = dto.telephone
        agence.codeAgence = dto.codeAgence
        agence.actif = dto.actif

        agenceRepository.save(agence)
        AgenceMapper.toDto(agence)
    }

    @Transactional
    void supprimer(Long id) {
        agenceRepository.deleteById(id)
    }

    /**
     * 🔥 Transfert d'une commande vers une autre agence
     */
    @Transactional
    void transfererCommande(Long commandeId, Long nouvelleAgenceId) {

        def commande = commandeRepository.findById(commandeId)
                .orElseThrow { new RuntimeException("Commande introuvable") }

        def nouvelleAgence = agenceRepository.findById(nouvelleAgenceId)
                .orElseThrow { new RuntimeException("Nouvelle agence introuvable") }

        commande.agence = nouvelleAgence

        commandeRepository.save(commande)
    }
}