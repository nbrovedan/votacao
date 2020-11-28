package br.com.brovetech.votacao.queue;

import br.com.brovetech.votacao.dto.ResultAgendaDTO;
import br.com.brovetech.votacao.exception.AgendaException;
import br.com.brovetech.votacao.helper.MessageHelper;
import br.com.brovetech.votacao.util.QueueTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static br.com.brovetech.votacao.enumeration.ErrorCodeEnum.ERROR_SEND_RESULT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class SendResultExchange {

    private final QueueTemplate queueTemplate;
    private final MessageHelper messageHelper;

    @Value("${default.votacao.result.exchange}")
    private String votacaoResultExchange;

    public void sendMessage(ResultAgendaDTO resultAgendaDTO) {
        try {
            queueTemplate.sendMessage(resultAgendaDTO, votacaoResultExchange);
        } catch (JsonProcessingException e) {
            throw new AgendaException(INTERNAL_SERVER_ERROR, messageHelper.get(ERROR_SEND_RESULT, e.getMessage()), e);
        }
    }
}
