package com.http200ok.finbuddy.product.domain;

import com.http200ok.finbuddy.bank.domain.Bank;
import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("SAVING")
@Getter @Setter
@NoArgsConstructor
public class SavingProduct extends FinancialProduct {
    // 적금 특화 필드 추가 가능
}
