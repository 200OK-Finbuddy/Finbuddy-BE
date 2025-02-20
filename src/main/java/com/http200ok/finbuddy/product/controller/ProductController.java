package com.http200ok.finbuddy.product.controller;

import com.http200ok.finbuddy.product.service.DepositProductService;
import com.http200ok.finbuddy.product.service.SavingProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final DepositProductService depositProductService;
    private final SavingProductService savingProductService;

    public ProductController(DepositProductService depositProductService, SavingProductService savingProductService) {
        this.depositProductService = depositProductService;
        this.savingProductService = savingProductService;
    }

    /**
     * 예금 데이터 수집 및 저장
     */
    @PostMapping("/deposits/fetch")
    public ResponseEntity<String> fetchDepositData() {
        depositProductService.fetchAndSaveDepositProducts();
        return ResponseEntity.ok("예금 데이터 저장 완료!");
    }

    /**
     * 적금 데이터 수집 및 저장
     */
    @PostMapping("/savings/fetch")
    public ResponseEntity<String> fetchSavingData() {
        savingProductService.fetchAndSaveSavingProducts();
        return ResponseEntity.ok("적금 데이터 저장 완료!");
    }
}
