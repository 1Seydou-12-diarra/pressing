package com.gestionPressing.demo.application.services

import com.gestionPressing.demo.application.dtos.EmployeRequestDto
import com.gestionPressing.demo.application.dtos.EmployeResponseDto
import com.gestionPressing.demo.application.mapper.EmployeMapper
import com.gestionPressing.demo.domain.ports.AgenceRepositoryPort
import com.gestionPressing.demo.domain.ports.EmployeRepositoryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmployeService {

    private final EmployeRepositoryPort employeRepository
    private final AgenceRepositoryPort agenceRepository

    EmployeService(EmployeRepositoryPort employeRepository,
                   AgenceRepositoryPort agenceRepository) {
        this.employeRepository = employeRepository
        this.agenceRepository = agenceRepository
   }

    @Transactional
    EmployeResponseDto creer(EmployeRequestDto dto) {
        def agence = agenceRepository.findById(dto.agenceId)
                .orElseThrow { new RuntimeException("Agence introuvable") }

        def employe = EmployeMapper.toEntity(dto, agence)
        employeRepository.save(employe)
        EmployeMapper.toDto(employe)
    }

    List<EmployeResponseDto> lister() {
        employeRepository.findAll().collect { EmployeMapper.toDto(it) }
    }

    @Transactional
    EmployeResponseDto modifier(Long id, EmployeRequestDto dto) {
        def employe = employeRepository.findById(id)
                .orElseThrow { new RuntimeException("Employé introuvable") }

        def agence = agenceRepository.findById(dto.agenceId)
                .orElseThrow { new RuntimeException("Agence introuvable") }

        employe.nom = dto.nom
        employe.prenom = dto.prenom
        employe.role = dto.role
        employe.keycloakId = dto.keycloakId
        employe.actif = dto.actif
        employe.agence = agence

        employeRepository.save(employe)
        EmployeMapper.toDto(employe)
    }

    @Transactional
    void supprimer(Long id) {
        employeRepository.deleteById(id)
    }

    Optional<EmployeResponseDto> findByKeycloakId(String keycloakId) {
        employeRepository.findByKeycloakId(keycloakId).map { EmployeMapper.toDto(it) }
    }

    List<EmployeResponseDto> findByAgence(Long agenceId) {
        employeRepository.findByAgenceId(agenceId).collect { EmployeMapper.toDto(it) }
    }
}
