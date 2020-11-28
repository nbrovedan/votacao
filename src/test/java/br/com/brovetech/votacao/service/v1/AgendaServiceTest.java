package br.com.brovetech.votacao.service.v1;

import br.com.brovetech.votacao.dto.AgendaCreateDTO;
import br.com.brovetech.votacao.entity.Agenda;
import br.com.brovetech.votacao.entity.Vote;
import br.com.brovetech.votacao.enumeration.VoteTypeEnum;
import br.com.brovetech.votacao.exception.AgendaException;
import br.com.brovetech.votacao.helper.MessageHelper;
import br.com.brovetech.votacao.repository.AgendaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static br.com.brovetech.votacao.enumeration.ErrorCodeEnum.AGENDA_ALREADY_OPEN;
import static br.com.brovetech.votacao.enumeration.ErrorCodeEnum.AGENDA_NOT_CLOSED;
import static java.lang.Boolean.FALSE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;
    @Mock
    private MessageHelper messageHelper;
    @InjectMocks
    private AgendaService agendaService;

    @Test
    public void shoudCreateAgenda(){
        var agendaCreate = AgendaCreateDTO.builder()
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .build();

        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .build();

        when(agendaRepository.save(any())).thenReturn(agenda);

        var agendaDTO = agendaService.create(agendaCreate);

        assertNotNull(agendaDTO);
        assertEquals("Pauta de teste", agendaDTO.getTitle());
        assertNull(agendaDTO.getOpenDate());
        assertNull(agendaDTO.getCloseDate());
        assertFalse(agendaDTO.getProcessed());
    }

    @Test
    public void shoudOpenAgenda(){
        var timeToClose = 10;
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .build();


        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any())).thenReturn(agenda);

        var agendaDTO = agendaService.open(1, timeToClose);

        assertNotNull(agendaDTO.getOpenDate());
        assertNotNull(agendaDTO.getCloseDate());
        assertEquals(agendaDTO.getCloseDate(), agendaDTO.getOpenDate().plusMinutes(timeToClose));
    }

    @Test
    public void shoudThrowExceptionOpenAgenda(){
        var timeToClose = 10;
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .openDate(LocalDateTime.now())
                .build();

        when(messageHelper.get(eq(AGENDA_ALREADY_OPEN), anyInt())).thenReturn(AGENDA_ALREADY_OPEN.getMessageKey());
        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));

        var agendaException = Assert.assertThrows(AgendaException.class, () -> agendaService.open(1, timeToClose));

        Assert.assertEquals(AGENDA_ALREADY_OPEN.getMessageKey(), agendaException.getMessage());
    }
    
    @Test
    public void shouldProcessResult(){
        var agendas = new ArrayList<Agenda>();
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .votes(Arrays.asList(Vote.builder()
                                .id(1)
                                .associatedCpf("19749993004")
                                .voteType(VoteTypeEnum.S)
                                .build(),
                        Vote.builder()
                                .id(2)
                                .associatedCpf("88740891046")
                                .voteType(VoteTypeEnum.N)
                                .build(),
                        Vote.builder()
                                .id(3)
                                .associatedCpf("83804217010")
                                .voteType(VoteTypeEnum.N)
                                .build(),
                        Vote.builder()
                                .id(4)
                                .associatedCpf("54837406025")
                                .voteType(VoteTypeEnum.S)
                                .build(),
                        Vote.builder()
                                .id(5)
                                .associatedCpf("74286170063")
                                .voteType(VoteTypeEnum.S)
                                .build()))
                .build();
        agendas.add(agenda);

        when(agendaRepository.findAllByCloseDateBeforeAndProcessed(any(), anyBoolean())).thenReturn(agendas);
        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));

        var processedAgendas = agendaService.processResult();

        assertEquals(1, processedAgendas.size());
        assertEquals(2, processedAgendas.get(0).getResult().size());
        assertEquals(Integer.valueOf(3), processedAgendas.get(0).getResult().get(VoteTypeEnum.S));
        assertEquals(Integer.valueOf(2), processedAgendas.get(0).getResult().get(VoteTypeEnum.N));
    }

    @Test
    public void shouldReturnResult(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .closeDate(LocalDateTime.now().minusMinutes(10))
                .processed(FALSE)
                .votes(Arrays.asList(Vote.builder()
                                .id(1)
                                .associatedCpf("19749993004")
                                .voteType(VoteTypeEnum.N)
                                .build(),
                        Vote.builder()
                                .id(2)
                                .associatedCpf("88740891046")
                                .voteType(VoteTypeEnum.N)
                                .build(),
                        Vote.builder()
                                .id(3)
                                .associatedCpf("83804217010")
                                .voteType(VoteTypeEnum.N)
                                .build(),
                        Vote.builder()
                                .id(4)
                                .associatedCpf("54837406025")
                                .voteType(VoteTypeEnum.N)
                                .build()))
                .build();

        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));

        var processedAgendas = agendaService.getResult(1);

        assertEquals(1, processedAgendas.getResult().size());
        assertNull(processedAgendas.getResult().get(VoteTypeEnum.S));
        assertEquals(Integer.valueOf(4), processedAgendas.getResult().get(VoteTypeEnum.N));
    }

    @Test
    public void shouldThrowExceptionReturnResult(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .votes(Collections.singletonList(Vote.builder()
                        .id(1)
                        .associatedCpf("19749993004")
                        .voteType(VoteTypeEnum.N)
                        .build()))
                .build();

        when(messageHelper.get(eq(AGENDA_NOT_CLOSED), anyInt())).thenReturn(AGENDA_NOT_CLOSED.getMessageKey());
        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));

        var agendaException = Assert.assertThrows(AgendaException.class, () -> agendaService.getResult(1));

        Assert.assertEquals(AGENDA_NOT_CLOSED.getMessageKey(), agendaException.getMessage());
    }

    @Test
    public void shouldReturnListAgenda(){
        var agendas = new ArrayList<Agenda>();
        agendas.add(Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .build());

        agendas.add(Agenda.builder()
                .id(2)
                .title("Pauta de teste 2")
                .description("Essa é uma pauta de teste 2")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .build());

        when(agendaRepository.findAll()).thenReturn(agendas);

        var agendaDTOS = agendaService.getAgendas();

        assertEquals(2, agendaDTOS.size());
    }

    @Test
    public void shouldReturnAgenda(){
        var agenda = Agenda.builder()
                .id(1)
                .title("Pauta de teste")
                .description("Essa é uma pauta de teste")
                .createDate(LocalDateTime.now())
                .processed(FALSE)
                .build();

        when(agendaRepository.findById(anyInt())).thenReturn(Optional.of(agenda));

        var agendaDTO = agendaService.getAgenda(1);

        assertEquals(agenda.getTitle(), agendaDTO.getTitle());
    }
}
