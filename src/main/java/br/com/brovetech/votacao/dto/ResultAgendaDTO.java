package br.com.brovetech.votacao.dto;

import br.com.brovetech.votacao.enumeration.VoteTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@With
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultAgendaDTO implements Serializable {

    private Integer id;
    private String title;
    private Map<VoteTypeEnum, Integer> result;
}
