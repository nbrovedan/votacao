package br.com.brovetech.votacao.service.v1;

import br.com.brovetech.votacao.dto.VoteDTO;
import br.com.brovetech.votacao.entity.Agenda;
import br.com.brovetech.votacao.entity.Vote;
import br.com.brovetech.votacao.exception.AgendaException;
import br.com.brovetech.votacao.helper.MessageHelper;
import br.com.brovetech.votacao.integration.userinfo.client.UserInfoIntegration;
import br.com.brovetech.votacao.integration.userinfo.dto.UserStatusDTO;
import br.com.brovetech.votacao.integration.userinfo.enumeration.UserStatusEnum;
import br.com.brovetech.votacao.repository.AgendaRepository;
import br.com.brovetech.votacao.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static br.com.brovetech.votacao.enumeration.ErrorCodeEnum.*;
import static br.com.brovetech.votacao.integration.userinfo.enumeration.UserStatusEnum.ABLE_TO_VOTE;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class VoteService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");
    private final AgendaRepository agendaRepository;
    private final VoteRepository voteRepository;
    private final MessageHelper messageHelper;
    private final UserInfoIntegration userInfoIntegration;

    public void vote(Integer id, VoteDTO voteDTO) {
        var userStatus = userInfoIntegration.getUserStatus(voteDTO.getCpf());

        cpfAbleToVoteValidation(userStatus);

        var agenda = agendaRepository.findById(id).orElseThrow(() -> new AgendaException(NOT_FOUND, messageHelper.get(AGENDA_NOT_FOUND, id)));

        agendaIsCloseValidation(agenda);
        agendaIsOpenValidation(agenda);
        cpfAlreadyVotingValidation(agenda, voteDTO.getCpf());

        voteRepository.save(Vote.builder()
                .associatedCpf(voteDTO.getCpf())
                .voteType(voteDTO.getVoteType())
                .agenda(agenda)
                .build());
    }

    private void agendaIsCloseValidation(Agenda agenda){
        var now = LocalDateTime.now(ZONE_ID);
        if(ofNullable(agenda.getCloseDate()).orElse(now).isBefore(now)){
            throw new AgendaException(BAD_REQUEST, messageHelper.get(VOTING_AGENDA_FINALIZED, agenda.getId()));
        }
    }

    private void cpfAbleToVoteValidation(UserStatusDTO userStatusDTO){
        if(!ABLE_TO_VOTE.equals(userStatusDTO.getStatus())){
            throw new AgendaException(BAD_REQUEST, messageHelper.get(CPF_NOT_ABLE_TO_VOTE));
        }
    }

    private void cpfAlreadyVotingValidation(Agenda agenda, String cpf){
        if(!voteRepository.findAllByAgenciaIdAndCPF(agenda.getId(), cpf).isEmpty()){
            throw new AgendaException(BAD_REQUEST, messageHelper.get(CPF_ALREADY_VOTED, agenda.getId()));
        }
    }

    private void agendaIsOpenValidation(Agenda agenda){
        if(isNull(agenda.getOpenDate())){
            throw new AgendaException(BAD_REQUEST, messageHelper.get(AGENDA_IS_NOT_OPEN, agenda.getId()));
        }
    }

}
