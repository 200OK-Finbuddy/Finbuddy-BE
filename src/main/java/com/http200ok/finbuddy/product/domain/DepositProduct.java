package com.http200ok.finbuddy.product.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("DEPOSIT")
@Getter @Setter
@NoArgsConstructor
public class DepositProduct extends FinancialProduct {
    // 예금 특화 필드 추가 가능
}
