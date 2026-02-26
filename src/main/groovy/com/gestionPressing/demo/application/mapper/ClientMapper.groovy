package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.ClientRequestDto
import com.gestionPressing.demo.application.dtos.ClientResponseDto
import com.gestionPressing.demo.domain.models.Client


class ClientMapper {

    static Client toDomain(ClientRequestDto dto) {
        new Client(
                nom: dto.nom,
                prenom: dto.prenom,
                telephone: dto.telephone,
                email: dto.email,
                adresse: dto.adresse
        )
    }

    static ClientResponseDto toDto(Client client) {
        new ClientResponseDto(
                id: client.id,
                nom: client.nom,
                prenom: client.prenom,
                telephone: client.telephone,
                email: client.email,
                adresse: client.adresse,
                pointsFidelite: client.pointsFidelite,
                actif: client.actif
        )
    }
}