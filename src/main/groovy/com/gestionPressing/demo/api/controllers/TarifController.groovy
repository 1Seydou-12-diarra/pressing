package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.TarifRequestDto
import com.gestionPressing.demo.application.dtos.TarifResponseDto
import com.gestionPressing.demo.application.services.TarifService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tarifs")
class TarifController {

    private final TarifService service

    TarifController(TarifService service) {
        this.service = service
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping
    TarifResponseDto creer(@RequestBody TarifRequestDto dto) {
        service.creer(dto)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/{id}")
    TarifResponseDto modifier(@PathVariable Long id,
                              @RequestBody TarifRequestDto dto) {
        service.modifier(id, dto)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    void supprimer(@PathVariable Long id) {
        service.supprimer(id)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER',ROLE_EMPLOYE)")
    @GetMapping
    List<TarifResponseDto> lister() {
        service.lister()
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CAISSIER',ROLE_EMPLOYE)")
    @GetMapping("/actif")
    BigDecimal obtenirTarif(
            @RequestParam String typeVetement,
            @RequestParam String typeService
    ) {
        service.obtenirTarif(typeVetement, typeService)
    }
}