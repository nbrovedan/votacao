package br.com.brovetech.votacao.dto;

import br.com.brovetech.votacao.enumeration.VoteTypeEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@With
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO implements Serializable {

    @NotBlank
    private String cpf;
    private VoteTypeEnum voteType;
}
