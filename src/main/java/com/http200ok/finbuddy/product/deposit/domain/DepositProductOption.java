package com.http200ok.finbuddy.product.deposit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class DepositProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_product_option_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "deposit_product_id", nullable = false)
    private DepositProduct depositProduct;

    @Column
    private String interestRateType; // 저축 금리 유형

    @Column
    private String interestRateTypeName; // 저축 금리 유형명

    @Column
    private Integer savingTerm; // 저축 기간 (개월 단위)

    @Column
    private Double interestRate; // 저축 금리

    @Column
    private Double maximumInterestRate; // 최고 우대 금리
}
