package com.digitalhouse.money.accountservice.data.enums;

public enum TransactionType {
    DEPÓSITO("Depósito em dinheiro"),
    TRANSFERÊNCIA("Transferência entre contas"),
    PAGAMENTO("Pagamento de contas");
    TransactionType(String label){
    }

}
