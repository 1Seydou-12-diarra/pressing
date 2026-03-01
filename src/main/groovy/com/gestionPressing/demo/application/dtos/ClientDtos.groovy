package com.gestionPressing.demo.application.dtos

class ClientRequestDto {
    String nom
    String prenom
    String telephone
    String email
    String adresse
}

class ClientResponseDto {
    Long id
    String nom
    String prenom
    String telephone
    String email
    String adresse
    Integer pointsFidelite
    Boolean actif
}