package br.com.brovetech.votacao.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@With
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AgendaCreateDTO implements Serializable {

    @NotBlank
    private String title;
    @Size(min = 10)
    private String description;
}
