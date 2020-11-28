package br.com.brovetech.votacao.exception;

import org.springframework.http.HttpStatus;

public class AgendaException extends BusinessException {

    public AgendaException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public AgendaException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
