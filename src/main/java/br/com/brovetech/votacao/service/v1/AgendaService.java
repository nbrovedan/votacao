package br.com.brovetech.votacao.service.v1;

import br.com.brovetech.votacao.dto.AgendaCreateDTO;
import br.com.brovetech.votacao.dto.AgendaDTO;
import br.com.brovetech.votacao.dto.ResultAgendaDTO;
import br.com.brovetech.votacao.entity.Agenda;
import br.com.brovetech.votacao.entity.Vote;
import br.com.brovetech.votacao.enumeration.VoteTypeEnum;
import br.com.brovetech.votacao.exception.AgendaException;
import br.com.brovetech.votacao.helper.MessageHelper;
import br.com.brovetech.votacao.mapper.AgendaMapper;
import br.com.brovetech.votacao.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.brovetech.votacao.enumeration.ErrorCodeEnum.*;
import static br.com.brovetech.votacao.mapper.AgendaMapper.toAgendaDTO;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    @Value("${default.votacao.section-time}")
    private Integer defaultSectionTime;

    private final AgendaRepository agendaRepository;
    private final MessageHelper messageHelper;

    public AgendaDTO create(AgendaCreateDTO agendaCreateDTO){
        var agendaCreated = agendaRepository.save(Agenda.builder()
                .title(agendaCreateDTO.getTitle())
                .description(agendaCreateDTO.getDescription())
                .processed(FALSE)
                .createDate(LocalDateTime.now(ZONE_ID))
                .lastUpdateDate(LocalDateTime.now(ZONE_ID))
                .build());

        return toAgendaDTO(agendaCreated);
    }

    public AgendaDTO open(Integer id, Integer time) {
        var agenda = agendaRepository.findById(id).orElseThrow(() -> new AgendaException(NOT_FOUND, messageHelper.get(AGENDA_NOT_FOUND, id)));

        agendaAlreadyOpenedValidation(agenda);

        agenda.setOpenDate(LocalDateTime.now(ZONE_ID));
        agenda.setCloseDate(LocalDateTime.now(ZONE_ID).plusMinutes(ofNullable(time).orElse(defaultSectionTime)));
        agendaRepository.save(agenda);
        return toAgendaDTO(agenda);
    }

    public List<ResultAgendaDTO> processResult(){
        var agendas = agendaRepository.findAllByCloseDateBeforeAndProcessed(LocalDateTime.now(ZONE_ID), FALSE);
        return agendas.stream().map(a -> processResult(a.getId())).collect(Collectors.toList());
    }

    public ResultAgendaDTO getResult(Integer id){
        var agenda = agendaRepository.findById(id).orElseThrow(() -> new AgendaException(NOT_FOUND, messageHelper.get(AGENDA_NOT_FOUND, id)));

        agendaNotClosedValidation(agenda);

        return getResult(agenda);
    }

    public List<AgendaDTO> getAgendas() {
        var agendas = agendaRepository.findAll();
        return agendas.stream().map(AgendaMapper::toAgendaDTO).collect(Collectors.toList());
    }

    public AgendaDTO getAgenda(Integer id){
        var agenda = agendaRepository.findById(id).orElseThrow(() -> new AgendaException(NOT_FOUND, messageHelper.get(AGENDA_NOT_FOUND, id)));
        return toAgendaDTO(agenda);
    }

    private ResultAgendaDTO processResult(Integer id){
        var agenda = agendaRepository.findById(id).orElseThrow(() -> new AgendaException(NOT_FOUND, messageHelper.get(AGENDA_NOT_FOUND, id)));
        var result = getResult(agenda);
        agenda.setProcessed(TRUE);
        agendaRepository.save(agenda);
        return result;
    }

    private ResultAgendaDTO getResult(Agenda agenda){
        Map<VoteTypeEnum, Integer> result = agenda.getVotes().stream().collect(Collectors.groupingBy(Vote::getVoteType, Collectors.summingInt(v -> 1)));
        return ResultAgendaDTO.builder()
                .id(agenda.getId())
                .title(agenda.getTitle())
                .result(result)
                .build();
    }

    private void agendaAlreadyOpenedValidation(Agenda agenda){
        if(nonNull(agenda.getOpenDate())){
            throw new AgendaException(BAD_REQUEST, messageHelper.get(AGENDA_ALREADY_OPEN, agenda.getId()));
        }
    }

    private void agendaNotClosedValidation(Agenda agenda){
        if(ofNullable(agenda.getCloseDate()).orElse(LocalDateTime.now(ZONE_ID)).compareTo(LocalDateTime.now(ZONE_ID)) >= 0){
            throw new AgendaException(BAD_REQUEST, messageHelper.get(AGENDA_NOT_CLOSED, agenda.getId()));
        }
    }
}
