package com.gestionPressing.demo.api.error

enum ErrorCode {

    // Génériques
    RESOURCE_NOT_FOUND,
    BAD_REQUEST,
    VALIDATION_ERROR,
    ACCESS_DENIED,
    INTERNAL_ERROR,

    // Spécifiques métier
    AGENCE_NOT_FOUND,
    COMMANDE_NOT_FOUND,
    EMPLOYE_NOT_FOUND,
    PAIEMENT_NOT_FOUND,
    TARIF_NOT_FOUND,
    STOCK_NOT_FOUND,
    CLIENT_NOT_FOUND
}