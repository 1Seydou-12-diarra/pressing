package com.gestionPressing.demo.application.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class CommandeRequest {
    Long clientId
    Long agenceId
    Long employeId

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dateRetraitPrevue

    List<ArticleRequest> articles = []
}

class ArticleRequest {
    String typeVetement
    String service
    String observations
    String codeBarres
}

class CommandeResponse {
    Long id
    Long clientId
    Long agenceId
    Long employeId
    String statut
    BigDecimal montantTotal

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dateDepot

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dateRetraitPrevue

    List<ArticleResponse> articles = []
}

class ArticleResponse {
    Long id
    String typeVetement
    String service
    BigDecimal tarifUnitaire
    String observations
    String codeBarres
    String statut
}