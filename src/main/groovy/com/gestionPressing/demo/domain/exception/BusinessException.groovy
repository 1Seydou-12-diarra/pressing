package com.gestionPressing.demo.domain.exception

import com.gestionPressing.demo.api.error.ErrorCode

class BusinessException extends RuntimeException {

    ErrorCode errorCode
    int httpStatus

    BusinessException(ErrorCode errorCode, String message, int httpStatus) {
        super(message)
        this.errorCode = errorCode
        this.httpStatus = httpStatus
    }
}
