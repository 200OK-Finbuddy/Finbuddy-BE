package com.http200ok.finbuddy.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("DEPOSIT")
@Getter @Setter
@NoArgsConstructor
public class DepositProductOption extends ProductOption {
    // 예금 옵션 특화 필드 추가 가능
}