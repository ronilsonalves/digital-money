package com.digitalhouse.money.accountservice.util;

import com.digitalhouse.money.accountservice.data.dto.MailMessageDTO;
import com.digitalhouse.money.accountservice.data.dto.UserResponse;
import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import com.digitalhouse.money.accountservice.data.repository.IUserFeignRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MailConstructor {

    private final IUserFeignRepository userFeignRepository;

    public MailMessageDTO getMailMessageAddCard(Account account, String cardEnding ) {
        UserResponse user = userFeignRepository.getUserByUUID(account.getUserId());

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.name()+", você adicionou um novo cartão à sua carteira")
                .body("<p>Parabéns "+user.name()+", o cartão final <b>"+cardEnding+"</b> foi adicionado com sucesso à" +
                        " sua carteira e já pode ser utilizá-lo para adicionar saldo à sua conta.</p></br>")
                .build();
    }

    public MailMessageDTO getMailMessageDelCard(Account account, String cardEnding) {
        UserResponse user = userFeignRepository.getUserByUUID(account.getUserId());

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject("Exclusão de cartão realizada")
                .body("<p>"+user.name()+", você removeu com sucesso de sua conta o cartão final "+cardEnding+"</p" +
                        "></br>" +
                        "<i><b>Lembrete:</b> é necessário ao menos um cartão ativo na conta para adição de " +
                        "saldo.</i>").build();
    }

    public MailMessageDTO getMailMessageAddMoney(Account account, Transaction transaction) {
        UserResponse user = userFeignRepository.getUserByUUID(account.getUserId());
        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.name()+", você adicionou dinheiro à sua conta!")
                .body("Olá "+user.name()+", você acabou de acionar R$ "+transaction.getTransactionAmount()+" à sua " +
                        "carteira usando um de seus cartões.<br>" +
                        "<b>Detalhes da transação:</b></br>" +
                        "<b>Cartão final:</b> "+transaction.getCardEnding()+"</br>" +
                        "<b>Valor do depósito:</b> "+transaction.getTransactionAmount()+"</br>" +
                        "<b>Data do depósito:</b> "+transaction.getTransactionDate()+"</br>" +
                        "<i>Use já seu saldo para realizar transferências entre contas</i>").build();
    }

    public MailMessageDTO getMailMessageTransferMoney(Account originAccount, Account recipientAccount,
                                                      Transaction transaction) {
        UserResponse from = userFeignRepository.getUserByUUID(originAccount.getUserId());
        UserResponse to = userFeignRepository.getUserByUUID(recipientAccount.getUserId());
        return MailMessageDTO.builder()
                .userResponse(from)
                .subject("Você enviou R$ "+transaction.getTransactionAmount()+" para "+to.name()+" "+to.lastName())
                .body("<p>Olá "+from.name()+", você enviou R$ "+transaction.getTransactionAmount()+" para a conta de " +
                        to.name()+".</p></br>" +
                        "<b>Detalhes da transação:</b></br>" +
                        "<p>" +
                        "<b>Nome do titular:</b> "+to.name()+" "+to.lastName()+ "</br>" +
                        "<b>N° da conta de destino:</b> "+recipientAccount.getId()+"</br>" +
                        "<b>Valor da transferência:</b> "+transaction.getTransactionAmount()+"</br>" +
                        "<b>Data da transação:</b> "+transaction.getTransactionAmount()+"</br></p>" +
                        "<i>Para consultar seu saldo após essa transação basta acessar o app da Digital Money</i>").build();
    }

}
