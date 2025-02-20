package com.http200ok.finbuddy.product.dto;

import com.http200ok.finbuddy.product.domain.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {
    private Long id;
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

    public ProductDto(Product product) {
        this.id = product.getId();
        this.bankName = product.getBank().getName();
        this.bankLogoUrl = product.getBank().getLogoUrl();
        this.code = product.getCode();
        this.name = product.getName();
        this.subscriptionMethod = product.getSubscriptionMethod();
        this.maturityInterestRate = product.getMaturityInterestRate();
        this.specialCondition = product.getSpecialCondition();
        this.subscriptionRestriction = product.getSubscriptionRestriction();
        this.subscriptionTarget = product.getSubscriptionTarget();
        this.additionalNotes = product.getAdditionalNotes();
        this.maximumLimit = product.getMaximumLimit();
        this.disclosureSubmissionMonth = product.getDisclosureSubmissionMonth();
        this.disclosureStartDate = product.getDisclosureStartDate();
        this.disclosureEndDate = product.getDisclosureEndDate();
        this.financialCompanySubmissionDate = product.getFinancialCompanySubmissionDate();
    }
}
