package com.gestionPressing.demo.application.dtos


import java.time.LocalDate
import java.math.BigDecimal

class TarifResponseDto {

    Long id
    String typeVetement
    String typeService
    BigDecimal prix
    Boolean actif
    LocalDate dateDebut
    LocalDate dateFin
}
