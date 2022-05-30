package br.ufc.quixada.npi.gestaocompetencia.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ExceptionResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private HttpStatus status;

    private String error;

    private String message;

    public static final class ExceptionResponseBuilder {
        private HttpStatus status;
        private String error;
        private String message;

        private ExceptionResponseBuilder() {
        }

        public static ExceptionResponseBuilder anExceptionResponseBuilder() {
            return new ExceptionResponseBuilder();
        }

        public ExceptionResponseBuilder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ExceptionResponseBuilder withError(String error) {
            this.error = error;
            return this;
        }

        public ExceptionResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ExceptionResponse build() {
            ExceptionResponse exceptionResponse = new ExceptionResponse();
            exceptionResponse.status = this.status;
            exceptionResponse.error = this.error;
            exceptionResponse.message = this.message;
            exceptionResponse.timestamp = LocalDateTime.now();
            return exceptionResponse;
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
