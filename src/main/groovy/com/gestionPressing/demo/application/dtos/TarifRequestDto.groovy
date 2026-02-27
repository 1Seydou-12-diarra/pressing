package com.gestionPressing.demo.application.dtos


import java.time.LocalDate
import java.math.BigDecimal

class TarifRequestDto {

    String typeVetement
    String typeService
    BigDecimal prix
    Boolean actif
    LocalDate dateDebut
    LocalDate dateFin
}