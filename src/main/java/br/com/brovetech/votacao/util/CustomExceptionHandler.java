package br.com.brovetech.votacao.util;

import br.com.brovetech.votacao.exception.BusinessException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<Object> businessExceptionHandler(BusinessException ex) {
        ApiError apiError = new ApiError(LocalDateTime.now(), ex.getRawStatusCode(), ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), ex.getStatus());
    }

    @ExceptionHandler(value = RestClientResponseException.class)
    public ResponseEntity<Object> responseStatusExceptionHandler(RestClientResponseException ex) {
        ApiError apiError = new ApiError(LocalDateTime.now(), ex.getRawStatusCode(), ex.getMessage(), ExceptionUtils.getStackTrace(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), requireNonNull(HttpStatus.resolve(ex.getRawStatusCode())));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception ex) {
        ApiError apiError = new ApiError(LocalDateTime.now(), INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ExceptionUtils.getStackTrace(ex));
        return new ResponseEntity<>(apiError, new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }
}
