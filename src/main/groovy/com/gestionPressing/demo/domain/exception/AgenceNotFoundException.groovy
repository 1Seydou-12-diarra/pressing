package com.gestionPressing.demo.domain.exception

import com.gestionPressing.demo.api.error.ErrorCode
import org.springframework.http.HttpStatus

class AgenceNotFoundException extends BusinessException {

    AgenceNotFoundException() {
        super(ErrorCode.AGENCE_NOT_FOUND,
                "Agence introuvable",
                HttpStatus.NOT_FOUND.value())
    }
}
