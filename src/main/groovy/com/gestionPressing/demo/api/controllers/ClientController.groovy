package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.ClientRequestDto
import com.gestionPressing.demo.application.dtos.ClientResponseDto
import com.gestionPressing.demo.domain.ports.input.ClientUseCase
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/clients")
class ClientController {

    private final ClientUseCase service

    ClientController(ClientUseCase service) {
        this.service = service
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EMPLOYE')")
    @PostMapping
    ClientResponseDto creer(@RequestBody ClientRequestDto dto) {
        ClientMapper.toDto(
                service.creer(ClientMapper.toDomain(dto))
        )
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EMPLOYE','ROLE_CAISSIER')")
    @GetMapping("/{id}")
    ClientResponseDto trouver(@PathVariable Long id) {
        ClientMapper.toDto(service.trouverParId(id))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping
    List<ClientResponseDto> rechercher(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String telephone
    ) {
        service.rechercher(nom, telephone)
                .collect { ClientMapper.toDto(it) }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    void desactiver(@PathVariable Long id) {
        service.desactiver(id)
    }
}