package br.com.brovetech.votacao.integration.userinfo.dto;


import br.com.brovetech.votacao.integration.userinfo.enumeration.UserStatusEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@With
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDTO {

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;
}
