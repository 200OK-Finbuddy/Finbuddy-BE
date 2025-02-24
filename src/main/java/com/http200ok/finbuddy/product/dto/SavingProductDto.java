package com.http200ok.finbuddy.product.dto;

import com.http200ok.finbuddy.product.domain.SavingProduct;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SavingProductDto {
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
    private List<SavingProductOptionDto> options;

    public SavingProductDto(SavingProduct savingProduct) {
        this.productId = savingProduct.getId();
        this.bankName = savingProduct.getBank().getName();
        this.bankLogoUrl = savingProduct.getBank().getLogoUrl();
        this.code = savingProduct.getCode();
        this.name = savingProduct.getName();
        this.subscriptionMethod = savingProduct.getSubscriptionMethod();
        this.maturityInterestRate = savingProduct.getMaturityInterestRate();
        this.specialCondition = savingProduct.getSpecialCondition();
        this.subscriptionRestriction = savingProduct.getSubscriptionRestriction();
        this.subscriptionTarget = savingProduct.getSubscriptionTarget();
        this.additionalNotes = savingProduct.getAdditionalNotes();
        this.maximumLimit = savingProduct.getMaximumLimit();
        this.disclosureSubmissionMonth = savingProduct.getDisclosureSubmissionMonth();
        this.disclosureStartDate = savingProduct.getDisclosureStartDate();
        this.disclosureEndDate = savingProduct.getDisclosureEndDate();
        this.financialCompanySubmissionDate = savingProduct.getFinancialCompanySubmissionDate();
        this.options = savingProduct.getOptions().stream()
                .map(SavingProductOptionDto::new)
                .collect(Collectors.toList());
    }
}
