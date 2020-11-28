package br.com.brovetech.votacao.service.v1;

import br.com.brovetech.votacao.dto.VoteDTO;
import br.com.brovetech.votacao.entity.Agenda;
import br.com.brovetech.votacao.entity.Vote;
import br.com.brovetech.votacao.exception.AgendaException;
import br.com.brovetech.votacao.helper.MessageHelper;
import br.com.brovetech.votacao.integration.userinfo.client.UserInfoIntegration;
import br.com.brovetech.votacao.integration.userinfo.dto.UserStatusDTO;
import br.com.brovetech.votacao.repository.AgendaRepository;
import br.com.brovetech.votacao.repository.VoteRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static br.com.brovetech.votacao.enumeration.ErrorCodeEnum.*;
import static br.com.brovetech.votacao.integration.userinfo.enumeration.UserStatusEnum.ABLE_TO_VOTE;
import static br.com.brovetech.votacao.integration.userinfo.enumeration.UserStatusEnum.UNABLE_TO_VOTE;
import static java.lang.Boolean.FALSE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceTest {

    @Mock
    private AgendaRepository agendaRepository;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private MessageHelper messageHelper;
    @Mock
    private UserInfoIntegration userInfoIntegration;
    @InjectMocks
    private VoteService voteService;

    @Test
    public void shouldRegisterVote(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .openDate(LocalDateTime.now())
                .build();

        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));
        when(userInfoIntegration.getUserStatus(anyString())).thenReturn(UserStatusDTO.builder().status(ABLE_TO_VOTE).build());
        when(voteRepository.save(any())).thenReturn(Vote.builder().build());

        voteService.vote(1, VoteDTO.builder().cpf("64259630008").build());
    }

    @Test
    public void shouldThrowExceptionRegisterVoteUnableToVote(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .openDate(LocalDateTime.now())
                .build();

        when(messageHelper.get(eq(CPF_NOT_ABLE_TO_VOTE))).thenReturn(CPF_NOT_ABLE_TO_VOTE.getMessageKey());
        when(userInfoIntegration.getUserStatus(anyString())).thenReturn(UserStatusDTO.builder().status(UNABLE_TO_VOTE).build());

        var agendaException = Assert.assertThrows(AgendaException.class, () -> voteService.vote(1, VoteDTO.builder().cpf("64259630008").build()));

        Assert.assertEquals(CPF_NOT_ABLE_TO_VOTE.getMessageKey(), agendaException.getMessage());
    }

    @Test
    public void shouldThrowExceptionRegisterVoteCPFNotFound(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .openDate(LocalDateTime.now())
                .build();

        when(userInfoIntegration.getUserStatus(anyString())).thenThrow(new AgendaException(HttpStatus.BAD_REQUEST, CPF_NOT_FOUND.getMessageKey()));

        var agendaException = Assert.assertThrows(AgendaException.class, () -> voteService.vote(1, VoteDTO.builder().cpf("642596300").build()));

        Assert.assertEquals(CPF_NOT_FOUND.getMessageKey(), agendaException.getMessage());
    }

    @Test
    public void shouldThrowExceptionRegisterVoteAgendaClose(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .closeDate(LocalDateTime.now().minusMinutes(1))
                .openDate(LocalDateTime.now())
                .build();

        when(messageHelper.get(eq(VOTING_AGENDA_FINALIZED), anyInt())).thenReturn(VOTING_AGENDA_FINALIZED.getMessageKey());
        when(userInfoIntegration.getUserStatus(anyString())).thenReturn(UserStatusDTO.builder().status(ABLE_TO_VOTE).build());
        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));

        var agendaException = Assert.assertThrows(AgendaException.class, () -> voteService.vote(1, VoteDTO.builder().cpf("64259630008").build()));

        Assert.assertEquals(VOTING_AGENDA_FINALIZED.getMessageKey(), agendaException.getMessage());
    }

    @Test
    public void shouldThrowExceptionRegisterVoteACPFAlreadyVoted(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .openDate(LocalDateTime.now())
                .build();

        when(messageHelper.get(eq(CPF_ALREADY_VOTED), anyInt())).thenReturn(CPF_ALREADY_VOTED.getMessageKey());
        when(userInfoIntegration.getUserStatus(anyString())).thenReturn(UserStatusDTO.builder().status(ABLE_TO_VOTE).build());
        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));
        when(voteRepository.findAllByAgenciaIdAndCPF(anyInt(), anyString())).thenReturn(Collections.singletonList(Vote.builder().id(1).build()));

        var agendaException = Assert.assertThrows(AgendaException.class, () -> voteService.vote(1, VoteDTO.builder().cpf("13437494090").build()));

        Assert.assertEquals(CPF_ALREADY_VOTED.getMessageKey(), agendaException.getMessage());
    }

    @Test
    public void shouldThrowExceptionRegisterVoteAgendaNotOpen(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .build();

        when(messageHelper.get(eq(AGENDA_IS_NOT_OPEN), anyInt())).thenReturn(AGENDA_IS_NOT_OPEN.getMessageKey());
        when(userInfoIntegration.getUserStatus(anyString())).thenReturn(UserStatusDTO.builder().status(ABLE_TO_VOTE).build());
        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));

        var agendaException = Assert.assertThrows(AgendaException.class, () -> voteService.vote(1, VoteDTO.builder().cpf("64259630008").build()));

        Assert.assertEquals(AGENDA_IS_NOT_OPEN.getMessageKey(), agendaException.getMessage());
    }
}
