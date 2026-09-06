package com.tpvgallega.backend.infrastructure.rest.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tpvgallega.backend.domain.exception.PedidoInvalidoException;
import com.tpvgallega.backend.domain.exception.PedidoNoEncontradoException;
import com.tpvgallega.backend.domain.exception.TransicionEstadoInvalidaException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({PedidoInvalidoException.class, TransicionEstadoInvalidaException.class})
    public ResponseEntity<ErrorResponse> handleSolicitudInvalida(RuntimeException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> "%s: %s".formatted(error.getField(), error.getDefaultMessage()))
                .orElse("Solicitud invalida");
        return construirRespuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNoEncontrado(PedidoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(new ErrorResponse(LocalDateTime.now(), status.value(), mensaje));
    }
}
