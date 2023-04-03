package com.digitalhouse.money.accountservice.util;

import com.digitalhouse.money.accountservice.data.dto.MailMessageDTO;
import com.digitalhouse.money.accountservice.data.dto.UserResponse;
import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import com.digitalhouse.money.accountservice.data.repository.IUserFeignRepository;
import com.digitalhouse.money.accountservice.service.PdfGenUploadService;
import com.lowagie.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MailConstructor {

    private final IUserFeignRepository userFeignRepository;

    private final PdfGenUploadService pdfService;

    private final SpringTemplateEngine templateEngine;

    public MailMessageDTO getMailMessageAddCard(Account account, String cardEnding, UUID cardId, String cardOwner) {
        UserResponse user = userFeignRepository.getUserByUUID(account.getUserId());
        String body = templateEngine.process("mail/add_card",buildContext(user,null,null,cardEnding,cardId.toString()
                ,cardOwner));

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.name()+", você adicionou um novo cartão à sua carteira")
                .body(body)
                .build();
    }

    public MailMessageDTO getMailMessageDelCard(Account account, String cardEnding, UUID cardId, String cardOwner) {
        UserResponse user = userFeignRepository.getUserByUUID(account.getUserId());
        String body = templateEngine.process("mail/del_card",buildContext(user,null,null,cardEnding,cardId.toString()
                ,cardOwner));

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject("Exclusão de cartão realizada")
                .body(body)
                .build();
    }

    public MailMessageDTO getMailMessageAddMoney(Account account, Transaction transaction, String cardOwner) throws DocumentException {
        UserResponse user = userFeignRepository.getUserByUUID(account.getUserId());
        Context context = buildContext(user,null,transaction,transaction.getCardEnding(),null,cardOwner);
        String body = templateEngine.process("mail/add_money",context);
        pdfService.generatePDFFile("pdf/deposit_receipt",context,account.getId(),transaction.getUuid());

        return MailMessageDTO.builder()
                .userResponse(user)
                .subject(user.name()+", você adicionou dinheiro à sua conta!")
                .body(body).build();
    }

    public MailMessageDTO getMailMessageTransferMoney(Account originAccount, Account recipientAccount,
                                                      Transaction transaction) throws DocumentException {
        UserResponse from = userFeignRepository.getUserByUUID(originAccount.getUserId());
        UserResponse to = userFeignRepository.getUserByUUID(recipientAccount.getUserId());
        Context context = buildContext(from,to,transaction,null,null,null);
        String body = templateEngine.process("mail/transfer_money",context);
        pdfService.generatePDFFile("pdf/transfer_receipt",context,originAccount.getId(),transaction.getUuid());

        return MailMessageDTO.builder()
                .userResponse(from)
                .subject("Comprovante de transferência")
                .body(body)
                .build();
    }

    private Context buildContext(UserResponse userResponse, UserResponse recipient, Transaction transaction,
                                 String cardEnding, String cardId, String cardOwner                                     ) {
        Context context = new Context();
        Map<String, Object> model = new HashMap<>();
        model.put("firstName",userResponse.name());
        model.put("transactionOwner",userResponse.name()+' '+userResponse.lastName());
        model.put("transactionNumber",transaction.getUuid());
        model.put("transactionDate",transaction.getTransactionDate());
        model.put("transactionOriginAccountNumber",transaction.getOriginAccountNumber());
        model.put("transactionRecipientAccountNumber",transaction.getRecipientAccountNumber());
        if (recipient != null)
            model.put("transactionRecipient",recipient.name()+' '+recipient.lastName());
        model.put("transactionCardEnding",transaction.getCardEnding());
        model.put("transactionDescription",transaction.getDescription());
        model.put("transactionAmount",transaction.getTransactionAmount());
        model.put("cardEnding",cardEnding);
        model.put("cardId",cardId);
        model.put("cardOwner",cardOwner);
        context.setVariables(model);
        return context;
    }
}
