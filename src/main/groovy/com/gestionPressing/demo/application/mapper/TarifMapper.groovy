package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.TarifRequestDto
import com.gestionPressing.demo.application.dtos.TarifResponseDto
import com.gestionPressing.demo.domain.models.Tarif


class TarifMapper {

    static Tarif toEntity(TarifRequestDto dto) {
        new Tarif(
                typeVetement: dto.typeVetement,
                typeService: dto.typeService,
                prix: dto.prix,
                actif: dto.actif,
                dateDebut: dto.dateDebut,
                dateFin: dto.dateFin
        )
    }

    static TarifResponseDto toDto(Tarif tarif) {
        new TarifResponseDto(
                id: tarif.id,
                typeVetement: tarif.typeVetement,
                typeService: tarif.typeService,
                prix: tarif.prix,
                actif: tarif.actif,
                dateDebut: tarif.dateDebut,
                dateFin: tarif.dateFin
        )
    }
}