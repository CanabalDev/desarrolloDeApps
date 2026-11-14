package com.gestionfincas.infraestructura.api;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ManejoErroresApi {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> manejarSolicitudInvalida(MethodArgumentNotValidException excepcion) {
        FieldError error = excepcion.getBindingResult().getFieldError();
        String mensaje = error == null
                ? "La solicitud contiene datos inválidos."
                : "El campo " + error.getField() + " no es válido.";
        return respuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaError> manejarCuerpoInvalido(HttpMessageNotReadableException excepcion) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no contiene un JSON válido.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespuestaError> manejarDatoInvalido(IllegalArgumentException excepcion) {
        return respuesta(HttpStatus.BAD_REQUEST, excepcion.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RespuestaError> manejarNoEncontrado(NoSuchElementException excepcion) {
        return respuesta(HttpStatus.NOT_FOUND, excepcion.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<RespuestaError> manejarConflicto(IllegalStateException excepcion) {
        return respuesta(HttpStatus.CONFLICT, excepcion.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaError> manejarConflictoPersistencia(DataIntegrityViolationException excepcion) {
        return respuesta(HttpStatus.CONFLICT, "La operación entra en conflicto con datos existentes.");
    }

    private ResponseEntity<RespuestaError> respuesta(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(new RespuestaError(mensaje));
    }

    public record RespuestaError(String mensaje) {
    }
}
