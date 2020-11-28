package br.com.brovetech.votacao.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {

    AGENDA_NOT_FOUND("agenda.not.found"),
    AGENDA_ALREADY_OPEN("agenda.already.open"),
    VOTING_AGENDA_FINALIZED("voting.agenda.finalized"),
    CPF_ALREADY_VOTED("cpf.already.voted"),
    AGENDA_IS_NOT_OPEN("agenda.is.not.open"),
    ERROR_SEND_RESULT("error.send.result"),
    AGENDA_NOT_CLOSED("agenda.not.closed"),
    CPF_NOT_ABLE_TO_VOTE("cpf.not.able.to.vote"),
    CPF_NOT_FOUND("cpf.not.found");

    private final String messageKey;
}
