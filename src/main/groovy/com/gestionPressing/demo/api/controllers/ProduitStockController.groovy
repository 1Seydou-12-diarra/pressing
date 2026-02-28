package com.gestionPressing.demo.api.controllers

import com.gestionPressing.demo.application.dtos.ProduitStockRequestDto
import com.gestionPressing.demo.application.dtos.ProduitStockResponseDto
import com.gestionPressing.demo.application.services.ProduitStockService
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/produits-stock")
class ProduitStockController {

    private final ProduitStockService service

    ProduitStockController(ProduitStockService service) {
        this.service = service
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping
    ProduitStockResponseDto creer(@RequestBody ProduitStockRequestDto dto) {
        service.creer(dto)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_EMPLOYE)")
    @GetMapping
    List<ProduitStockResponseDto> lister() {
        service.lister()
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/{id}")
    ProduitStockResponseDto modifier(@PathVariable Long id,
                                     @RequestBody ProduitStockRequestDto dto) {
        service.modifier(id, dto)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    void supprimer(@PathVariable Long id) {
        service.supprimer(id)
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/seuil-bas")
    List<ProduitStockResponseDto> produitsSeuilBas() {
        service.produitsSeuilBas()
    }
}
