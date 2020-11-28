package br.com.brovetech.votacao.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiError {

    private final LocalDateTime timestamp;
    private final Integer status;
    private final String error;
    private String trace;
}
