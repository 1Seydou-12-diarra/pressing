package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.EmployeRequestDto
import com.gestionPressing.demo.application.dtos.EmployeResponseDto
import com.gestionPressing.demo.application.services.EmployeService
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/employes")
class EmployeController {

    private final EmployeService service

    EmployeController(EmployeService service) {
        this.service = service
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping
    EmployeResponseDto creer(@RequestBody EmployeRequestDto dto) {
        service.creer(dto)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_EMPLOYE')")
    @GetMapping
    List<EmployeResponseDto> lister() {
        service.lister()
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/{id}")
    EmployeResponseDto modifier(@PathVariable Long id,
                                @RequestBody EmployeRequestDto dto) {
        service.modifier(id, dto)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    void supprimer(@PathVariable Long id) {
        service.supprimer(id)
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER',ROLE_EMPLOYE)")
    @GetMapping("/keycloak/{keycloakId}")
    EmployeResponseDto findByKeycloak(@PathVariable String keycloakId) {
        service.findByKeycloakId(keycloakId)
                .orElseThrow { new RuntimeException("Employé introuvable") }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/agence/{agenceId}")
    List<EmployeResponseDto> findByAgence(@PathVariable Long agenceId) {
        service.findByAgence(agenceId)
    }
}