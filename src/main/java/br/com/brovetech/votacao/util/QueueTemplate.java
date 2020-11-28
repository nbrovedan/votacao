package br.com.brovetech.votacao.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueTemplate {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Object messageObject, String exchange) throws JsonProcessingException {
        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        Message message = MessageBuilder.withBody(new ObjectMapper().writeValueAsBytes(messageObject)).andProperties(props).build();
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.convertAndSend(message);
    }
}
