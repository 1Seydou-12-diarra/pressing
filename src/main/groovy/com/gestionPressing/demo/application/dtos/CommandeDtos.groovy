package com.gestionPressing.demo.application.dtos

import com.gestionPressing.demo.domain.enums.StatutArticle
import com.gestionPressing.demo.domain.enums.StatutCommande
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDateTime

// ─── REQUEST ────────────────────────────────────────────────
class CreerCommandeRequest {

    @NotNull(message = "Le client est obligatoire")
    Long clientId

    Long agenceId
    Long employeId

    @NotEmpty(message = "La commande doit contenir au moins un article")
    @Valid
    List<ArticleCommandeRequest> articles

    LocalDateTime dateRetraitPrevue
}

class ArticleCommandeRequest {

    @NotBlank(message = "Le type de vêtement est obligatoire")
    String typeVetement

    @NotBlank(message = "Le service est obligatoire")
    String service

    @NotNull(message = "Le tarif unitaire est obligatoire")
    @DecimalMin(value = "0.0", message = "Le tarif doit être positif")
    BigDecimal tarifUnitaire

    String observations

    @NotBlank(message = "Le code-barres est obligatoire")
    String codeBarres
}

class ChangerStatutRequest {

    @NotNull(message = "Le nouveau statut est obligatoire")
    StatutCommande nouveauStatut

    Long employeId
}

// ─── RESPONSE ───────────────────────────────────────────────
class CommandeResponse {

    Long id
    Long clientId
    Long agenceId
    Long employeId
    StatutCommande statut
    BigDecimal montantTotal
    LocalDateTime dateDepot
    LocalDateTime dateRetraitPrevue
    List<ArticleCommandeResponse> articles
}

class ArticleCommandeResponse {

    Long id
    String typeVetement
    String service
    BigDecimal tarifUnitaire
    String observations
    String codeBarres
    StatutArticle statut
}