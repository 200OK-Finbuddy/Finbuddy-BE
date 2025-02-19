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
public class DepositProduct extends FinancialProduct {
    // 예금 특화 필드 추가 가능
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductOption> options = new ArrayList<>();

    // 옵션 추가 편의 메소드
    public void addOption(ProductOption option) {
        options.add(option);
        option.setProduct(this);
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
