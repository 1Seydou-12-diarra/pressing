package com.gestionPressing.demo.api.error


import java.time.LocalDateTime
class ErrorResponse {

    String code
    String message
    int status
    LocalDateTime timestamp = LocalDateTime.now()
}
