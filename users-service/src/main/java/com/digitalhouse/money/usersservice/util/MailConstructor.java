package com.digitalhouse.money.usersservice.util;

import com.digitalhouse.money.usersservice.api.request.MailMessageDTO;
import com.digitalhouse.money.usersservice.api.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MailConstructor {

    public MailMessageDTO getMailMessageUserRegistered(UserResponse user, String code) {
        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.getName()+", ative já sua conta!")
                .body("<p> Parabéns "+user.getName()+", você está a um passo de usufruir de todos os benefícios da " +
                        "Digital Money e só precisa verificar seu e-mail.<p></br>" +
                        "<p>Para ativar sua conta, digite o código abaixo:</p></br>" +
                        "<h3>"+code+"</h3></br></br>" +
                        "<i><b>Atenção</b>: para ter acesso a conta é necessário a validação do seu email com o " +
                        "código acima, o mesmo tem validade de até 15 minutos.<i>").build();
    }

    public MailMessageDTO getMailMessageResendCode(UserResponse user, String code) {
        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.getName()+", seu código de verificação chegou!")
                .body("<p> "+user.getName()+", estamos te enviando novamente um novo código de verificação.</p></br>" +
                        "<p><b><i>Ninguém da Digital Money te pedirá esse código, não o compartilhe com ninguém!</b></i></p></br>" +
                        "<p>Para ativar sua conta, digite o código abaixo:</p></br>" +
                        "<h3>"+code+"</h3></br></br>" +
                        "<i><b>Atenção</b>: para ter acesso a conta é necessário a validação do seu email com o " +
                        "código acima, o mesmo tem validade de até 15 minutos.<i>").build();
    }

    public MailMessageDTO getEmailMessageChangedEmail(UserResponse user, String code) {
        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.getName()+", verifique seu novo endereço de e-mail!")
                .body("<p> "+user.getName()+", para efetivar a alteração do enderço de e-mail da sua conta você " +
                        "precisa validá-lo.</p></br>" +
                        "<p><b><i>Ninguém da Digital Money te pedirá esse código, não o compartilhe com ninguém!</b></i></p></br>" +
                        "<p>Para ativar sua conta, digite o código abaixo:</p></br>" +
                        "<h3>"+code+"</h3></br></br>" +
                        "<i><b>Atenção</b>: para ter acesso a conta é necessário a validação do seu email com o " +
                        "código acima, o mesmo tem validade de até 15 minutos.<i>").build();
    }

    public MailMessageDTO getEmailMessageEmailVerified(UserResponse user) {
        return MailMessageDTO.builder()
                .userResponse(user)
                .subject("Seu endereço de e-mail agora está verificado.")
                .body("<p>"+user.getName()+", informamos que seu endereço de e-mail está agora verificado e você tem " +
                        "total acesso às funcionalidades da sua conta na Digital Money.</p></br>" +
                        "<p><i>Adicione agora mesmo um novo cartão à sua conta e comece a depositar dinheiro em sua " +
                        "carteira de forma rápida e prática</i></p>").build();
    }

}
