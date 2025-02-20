package com.http200ok.finbuddy.account.dto;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.transaction.dto.TransactionResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AccountDetailsResponse {

    private Long accountId;
    private String accountName;
    private Long balance;
    private List<TransactionResponse> transactions;

    public AccountDetailsResponse(Account account) {
        this.accountId = account.getId();
        this.accountName = account.getAccountName();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }
}
