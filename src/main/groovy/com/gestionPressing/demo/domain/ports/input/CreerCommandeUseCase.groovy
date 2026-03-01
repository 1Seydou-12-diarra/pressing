package com.gestionPressing.demo.domain.ports.input


import com.gestionPressing.demo.domain.models.Commande

import java.time.LocalDateTime

interface CreerCommandeUseCase {
    Commande creer(Long clientId, Long agenceId, Long employeId, List articles, LocalDateTime dateRetraitPrevue)
}