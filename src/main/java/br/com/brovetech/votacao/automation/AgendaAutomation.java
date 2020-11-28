package br.com.brovetech.votacao.automation;

import br.com.brovetech.votacao.queue.SendResultExchange;
import br.com.brovetech.votacao.service.v1.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgendaAutomation {

    private final AgendaService agendaService;
    private final SendResultExchange sendResultExchange;

    @Scheduled(fixedDelayString = "${default.automation.agenda}")
    public void processResult(){
        var agendas = agendaService.processResult();
        agendas.forEach(sendResultExchange::sendMessage);
    }
}
