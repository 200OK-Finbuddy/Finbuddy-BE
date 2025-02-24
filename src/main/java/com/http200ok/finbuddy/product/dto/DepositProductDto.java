package com.http200ok.finbuddy.product.dto;

import com.http200ok.finbuddy.product.domain.DepositProduct;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DepositProductDto {
    private Long productId;
    private String bankName;
    private String bankLogoUrl;
    private String code;
    private String name;
    private String subscriptionMethod;
    private String maturityInterestRate;
    private String specialCondition;
    private String subscriptionRestriction;
    private String subscriptionTarget;
    private String additionalNotes;
    private Long maximumLimit;
    private LocalDate disclosureSubmissionMonth;
    private LocalDate disclosureStartDate;
    private LocalDate disclosureEndDate;
    private LocalDateTime financialCompanySubmissionDate;
    private List<DepositProductOptionDto> options; // 옵션 리스트

    public DepositProductDto(DepositProduct depositProduct) {
        this.productId = depositProduct.getId();
        this.bankName = depositProduct.getBank().getName();
        this.bankLogoUrl = depositProduct.getBank().getLogoUrl();
        this.code = depositProduct.getCode();
        this.name = depositProduct.getName();
        this.subscriptionMethod = depositProduct.getSubscriptionMethod();
        this.maturityInterestRate = depositProduct.getMaturityInterestRate();
        this.specialCondition = depositProduct.getSpecialCondition();
        this.subscriptionRestriction = depositProduct.getSubscriptionRestriction();
        this.subscriptionTarget = depositProduct.getSubscriptionTarget();
        this.additionalNotes = depositProduct.getAdditionalNotes();
        this.maximumLimit = depositProduct.getMaximumLimit();
        this.disclosureSubmissionMonth = depositProduct.getDisclosureSubmissionMonth();
        this.disclosureStartDate = depositProduct.getDisclosureStartDate();
        this.disclosureEndDate = depositProduct.getDisclosureEndDate();
        this.financialCompanySubmissionDate = depositProduct.getFinancialCompanySubmissionDate();
        this.options = depositProduct.getOptions().stream()
                .map(DepositProductOptionDto::new)
                .collect(Collectors.toList());
    }
}
