package com.http200ok.finbuddy.product.domain;


import com.http200ok.finbuddy.bank.domain.Bank;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DEPOSIT")
@Getter @Setter
@NoArgsConstructor
public class DepositProduct extends Product {

    @OneToMany(mappedBy = "depositProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepositProductOption> options = new ArrayList<>();

    // 옵션 추가 편의 메소드
    public void addOption(DepositProductOption option) {
        options.add(option);
        option.setDepositProduct(this);
    }

    public static DepositProduct createProduct(Bank bank, String code, String name, String subscriptionMethod,
                                               String maturityInterestRate, String specialCondition,
                                               String subscriptionRestriction, String subscriptionTarget,
                                               String additionalNotes, Long maximumLimit) {
        DepositProduct product = new DepositProduct();
        product.bank = bank;
        product.code = code;
        product.name = name;
        product.subscriptionMethod = subscriptionMethod;
        product.maturityInterestRate = maturityInterestRate;
        product.specialCondition = specialCondition;
        product.subscriptionRestriction = subscriptionRestriction;
        product.subscriptionTarget = subscriptionTarget;
        product.additionalNotes = additionalNotes;
        product.maximumLimit = maximumLimit;
        return product;
    }
}
