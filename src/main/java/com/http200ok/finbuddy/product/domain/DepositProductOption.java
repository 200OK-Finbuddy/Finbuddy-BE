package com.http200ok.finbuddy.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("DEPOSIT")
@Getter @Setter
@NoArgsConstructor
public class DepositProductOption extends ProductOption {
    // 예금 옵션 특화 필드 추가 가능

    public static DepositProductOption createDepositProductOption(FinancialProduct product,
                                                                  String interestRateType,
                                                                  String interestRateTypeName,
                                                                  Integer savingTerm,
                                                                  Double interestRate,
                                                                  Double maximumInterestRate) {
        DepositProductOption option = new DepositProductOption();
        option.setProduct(product);
        option.setInterestRateType(interestRateType);
        option.setInterestRateTypeName(interestRateTypeName);
        option.setSavingTerm(savingTerm);
        option.setInterestRate(interestRate);
        option.setMaximumInterestRate(maximumInterestRate);
        return option;
    }
}

