package br.com.brovetech.votacao.mapper;

import br.com.brovetech.votacao.dto.AgendaDTO;
import br.com.brovetech.votacao.entity.Agenda;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AgendaMapper {

    public static AgendaDTO toAgendaDTO(Agenda agenda){
        return AgendaDTO.builder()
                .id(agenda.getId())
                .title(agenda.getTitle())
                .description(agenda.getDescription())
                .createDate(agenda.getCreateDate())
                .openDate(agenda.getOpenDate())
                .closeDate(agenda.getCloseDate())
                .processed(agenda.getProcessed())
                .build();
    }
}
