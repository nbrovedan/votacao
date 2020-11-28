package br.com.brovetech.votacao.resource.v1;

import br.com.brovetech.votacao.dto.AgendaCreateDTO;
import br.com.brovetech.votacao.dto.AgendaDTO;
import br.com.brovetech.votacao.dto.ResultAgendaDTO;
import br.com.brovetech.votacao.dto.VoteDTO;
import br.com.brovetech.votacao.service.v1.AgendaService;
import br.com.brovetech.votacao.service.v1.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.Servers;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/v1/agenda", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Server(url = "/v1")
public class AgendaResource {

    private final AgendaService agendaService;
    private final VoteService voteService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar uma pauta")
    public AgendaDTO create(@Valid @RequestBody AgendaCreateDTO agendaCreateDTO){
        return agendaService.create(agendaCreateDTO);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar pautas")
    public List<AgendaDTO> getAgendas(){
        return agendaService.getAgendas();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar pauta por id")
    public AgendaDTO getAgenda(@PathVariable("id") Integer id){
        return agendaService.getAgenda(id);
    }

    @PutMapping(value = "/{id}/open", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @Operation(summary = "Abrir uma pauta para votação")
    public AgendaDTO open(@PathVariable("id") Integer id, @RequestParam(value = "time", required = false) Integer time) {
        return agendaService.open(id, time);
    }

    @PutMapping(value = "/{id}/vote", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(ACCEPTED)
    @Operation(summary = "Votar em uma pauta")
    public void vote(@PathVariable("id") Integer id, @Valid @RequestBody VoteDTO voteDTO) {
        voteService.vote(id, voteDTO);
    }

    @GetMapping(value = "/{id}/result", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Buscar resultado")
    public ResultAgendaDTO getResult(@PathVariable("id") Integer id){
        return agendaService.getResult(id);
    }
}
