package ru.itmo.coffee.store.controller.advice

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.itmo.coffee.MessageResponse
import javax.persistence.EntityNotFoundException

@ControllerAdvice
open class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [EntityNotFoundException::class])
    protected fun handleNotFound(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, MessageResponse(ex.message!!),
            HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    protected fun handleRuntime(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, MessageResponse(ex.message!!),
            HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }
}