package com.http200ok.finbuddy.product.domain;

import com.http200ok.finbuddy.bank.domain.Bank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class FinancialProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    protected Bank bank;

    @Column(nullable = false)
    protected String code;

    @Column(nullable = false)
    protected String name;

    protected String subscriptionMethod;
    protected String maturityInterestRate;
    protected String specialCondition;
    protected String subscriptionRestriction;
    protected String subscriptionTarget;
    protected String additionalNotes;
    protected Long maximumLimit;

}