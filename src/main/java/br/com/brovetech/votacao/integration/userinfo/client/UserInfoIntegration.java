package br.com.brovetech.votacao.integration.userinfo.client;

import br.com.brovetech.votacao.exception.AgendaException;
import br.com.brovetech.votacao.helper.MessageHelper;
import br.com.brovetech.votacao.integration.userinfo.dto.UserStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static br.com.brovetech.votacao.enumeration.ErrorCodeEnum.CPF_NOT_FOUND;
import static org.springframework.http.HttpStatus.*;

@Component
@RequiredArgsConstructor
public class UserInfoIntegration {

    @Value("${api.integrations.user-info.host}${api.integrations.user-info.user.base-path}")
    private String host;
    @Value("${api.integrations.user-info.user.find-cpf}")
    private String findCpf;

    private final RestTemplate restTemplate;
    private final MessageHelper messageHelper;

    @GetMapping("${api.integrations.user-info.user.find-cpf}")
    public UserStatusDTO getUserStatus(String cpf){
        try {
            return restTemplate.getForObject(host.concat(findCpf.replace("{cpf}", cpf)), UserStatusDTO.class);
        } catch (HttpClientErrorException ex) {
            if(NOT_FOUND.equals(ex.getStatusCode())){
                throw new AgendaException(BAD_REQUEST, messageHelper.get(CPF_NOT_FOUND, cpf));
            }
            throw new AgendaException(ex.getStatusCode(), ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new AgendaException(INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
