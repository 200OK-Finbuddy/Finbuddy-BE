package com.http200ok.finbuddy.product.domain;

import com.http200ok.finbuddy.bank.domain.Bank;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("SAVING")
@Getter @Setter
@NoArgsConstructor
public class SavingProduct extends Product {

    @OneToMany(mappedBy = "savingProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavingProductOption> options = new ArrayList<>();

    // 옵션 추가 편의 메소드
    public void addOption(SavingProductOption option) {
        options.add(option);
        option.setSavingProduct(this);
    }

    public static SavingProduct createProduct(Bank bank, String code, String name, String subscriptionMethod,
                                              String maturityInterestRate, String specialCondition,
                                              String subscriptionRestriction, String subscriptionTarget,
                                              String additionalNotes, Long maximumLimit) {
        SavingProduct product = new SavingProduct();
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
