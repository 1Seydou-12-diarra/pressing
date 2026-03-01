package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.AgenceRequestDto
import com.gestionPressing.demo.application.dtos.AgenceResponseDto
import com.gestionPressing.demo.domain.models.Agence



class AgenceMapper {

    static Agence toEntity(AgenceRequestDto dto) {
        new Agence(
                nom: dto.nom,
                adresse: dto.adresse,
                telephone: dto.telephone,
                codeAgence: dto.codeAgence,
                actif: dto.actif
        )
    }

    static AgenceResponseDto toDto(Agence a) {
        new AgenceResponseDto(
                id: a.id,
                nom: a.nom,
                adresse: a.adresse,
                telephone: a.telephone,
                codeAgence: a.codeAgence,
                actif: a.actif
        )
    }
}
