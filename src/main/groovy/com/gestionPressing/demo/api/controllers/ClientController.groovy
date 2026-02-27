package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.ClientRequestDto
import com.gestionPressing.demo.application.dtos.ClientResponseDto
import com.gestionPressing.demo.application.mapper.ClientMapper
import com.gestionPressing.demo.domain.ports.input.ClientUseCase
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/clients")
class ClientController {

    private final ClientUseCase service
    private final ClientMapper mapper

    ClientController(ClientUseCase service, ClientMapper mapper) {
        this.service = service
        this.mapper = mapper
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EMPLOYE')")
    @PostMapping
    ClientResponseDto creer(@RequestBody ClientRequestDto dto) {
        mapper.toDto(service.creer(mapper.toDomain(dto)))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EMPLOYE','ROLE_CAISSIER')")
    @GetMapping("/{id}")
    ClientResponseDto trouver(@PathVariable Long id) {
        mapper.toDto(service.trouverParId(id))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping
    List<ClientResponseDto> rechercher(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String telephone
    ) {
        service.rechercher(nom, telephone)
                .collect { mapper.toDto(it) }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    void desactiver(@PathVariable Long id) {
        service.desactiver(id)
    }
    //@PreAuthorize("hasAnyRole('ADMIN','EMPLOYE','CAISSIER')")
   // @GetMapping("/{id}/commandes")
    //List<CommandeResponseDto> historique(@PathVariable Long id) {
        //service.historiqueCommandes(id)
                //.collect { CommandeMapper.toDto(it) }
    //}


    @PreAuthorize("hasAnyRole('ADMIN','CAISSIER')")
    @PostMapping("/{id}/fidelite/credit")
    void crediter(
            @PathVariable Long id,
            @RequestParam BigDecimal montant
    ) {
        service.crediterPoints(id, montant)
    }

    @PreAuthorize("hasAnyRole('ADMIN','CAISSIER')")
    @PostMapping("/{id}/fidelite/utiliser")
    void utiliser(
            @PathVariable Long id,
            @RequestParam int points
    ) {
        service.utiliserPoints(id, points)
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYE')")
    @GetMapping("/{id}/fidelite")
    int solde(@PathVariable Long id) {
        service.trouverParId(id).pointsFidelite
    }
}