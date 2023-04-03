package com.digitalhouse.money.usersservice.util;

import com.digitalhouse.money.usersservice.api.request.MailMessageDTO;
import com.digitalhouse.money.usersservice.api.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class MailConstructor {

    private final TemplateEngine templateEngine;

    public MailMessageDTO getMailMessageUserRegistered(UserResponse user, String code) {
        String body = templateEngine.process("mail/activation_email",buildContext(user, code));

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.getName()+", ative já sua conta!")
                .body(body)
                .build();
    }

    public MailMessageDTO getMailMessageResendCode(UserResponse user, String code) {
        String body = templateEngine.process("mail/resend_code",buildContext(user, code));

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.getName()+", seu código de verificação chegou!")
                .body(body)
                .build();
    }

    public MailMessageDTO getEmailMessageChangedEmail(UserResponse user, String code) {
        String body = templateEngine.process("mail/resend_code",buildContext(user, code));

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.getName()+", verifique seu novo endereço de e-mail!")
                .body(body)
                .build();
    }

    public MailMessageDTO getEmailMessageEmailVerified(UserResponse user) {
        String body = templateEngine.process("mail/email_activated",buildContext(user, null));

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject("Seu endereço de e-mail agora está verificado.")
                .body(body)
                .build();
    }

    private Context buildContext(UserResponse user, String verificationCode) {
        Context context = new Context();
        Map<String, Object> model = new HashMap<>();
        model.put("firstName",user.getName());
        model.put("verificationCode",verificationCode);

        context.setVariables(model);
        return context;
    }

}
