package br.com.brovetech.votacao.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@With
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDTO implements Serializable {

    private Integer id;
    private String title;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private Boolean processed;
}

