package br.com.brovetech.votacao.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VoteTypeEnum {

    S("Sim"), N("Não");

    private final String description;
}
