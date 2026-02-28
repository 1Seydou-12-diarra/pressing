package com.gestionPressing.demo.application.mapper

import com.gestionPressing.demo.application.dtos.EmployeRequestDto
import com.gestionPressing.demo.application.dtos.EmployeResponseDto
import com.gestionPressing.demo.domain.models.Employe


class EmployeMapper {

    static Employe toEntity(EmployeRequestDto dto, def agence) {
        new Employe(
                keycloakId: dto.keycloakId,
                nom: dto.nom,
                prenom: dto.prenom,
                role: dto.role,
                agence: agence,
                actif: dto.actif
        )
    }

    static EmployeResponseDto toDto(Employe e) {
        new EmployeResponseDto(
                id: e.id,
                keycloakId: e.keycloakId,
                nom: e.nom,
                prenom: e.prenom,
                role: e.role,
                agenceId: e.agence?.id,
                actif: e.actif
        )
    }
}