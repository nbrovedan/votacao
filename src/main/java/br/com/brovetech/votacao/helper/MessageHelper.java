package br.com.brovetech.votacao.helper;

import br.com.brovetech.votacao.enumeration.ErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import javax.annotation.PostConstruct;
import java.util.Locale;

@RequiredArgsConstructor
public class MessageHelper {

    private final MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    public void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    public String get(String code, Object... args) {
        return accessor.getMessage(code, args);
    }

    public String get(ErrorCodeEnum code, Object... args) {
        return accessor.getMessage(code.getMessageKey(), args);
    }
}
