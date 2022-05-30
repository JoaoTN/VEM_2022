package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> resourceAlreadyExist(ResourceAlreadyExistsException ex) {
        ExceptionResponse response = ExceptionResponse.ExceptionResponseBuilder.anExceptionResponseBuilder()
                .withStatus(HttpStatus.CONFLICT)
                .withError(HttpStatus.CONFLICT.name())
                .withMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ExceptionResponse response = ExceptionResponse.ExceptionResponseBuilder.anExceptionResponseBuilder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withError(HttpStatus.NOT_FOUND.name())
                .withMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ExceptionResponse> notAllowed(NotAllowedException ex) {
        ExceptionResponse response = ExceptionResponse.ExceptionResponseBuilder.anExceptionResponseBuilder()
                .withStatus(HttpStatus.FORBIDDEN)
                .withError(HttpStatus.NOT_FOUND.name())
                .withMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionResponse> fileUpload(FileUploadException ex) {
        ExceptionResponse response = ExceptionResponse.ExceptionResponseBuilder.anExceptionResponseBuilder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withError("File Upload Error")
                .withMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }
    
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionResponse> handleArgumentInvalid(Exception ex) {
        ExceptionResponse response = ExceptionResponse.ExceptionResponseBuilder.anExceptionResponseBuilder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withError(HttpStatus.BAD_REQUEST.name())
                .withMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }    

    @ExceptionHandler({Exception.class, GestaoCompetenciaException.class})
    public ResponseEntity<ExceptionResponse> handleAll(Exception ex) {
        ExceptionResponse response = ExceptionResponse.ExceptionResponseBuilder.anExceptionResponseBuilder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withError(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .withMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(value = {InvalidJwtAuthenticationException.class})
    public ResponseEntity<Object> invalidJwtAuthentication(InvalidJwtAuthenticationException ex, WebRequest request) {
        return status(UNAUTHORIZED).build();
    }

}
