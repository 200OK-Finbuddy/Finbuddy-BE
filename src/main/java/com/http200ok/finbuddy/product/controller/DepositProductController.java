package com.http200ok.finbuddy.product.controller;

import com.http200ok.finbuddy.product.service.DepositProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deposits")
public class DepositProductController {
    private final DepositProductService depositProductService;

    public DepositProductController(DepositProductService depositProductService) {
        this.depositProductService = depositProductService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchDepositData() {
        depositProductService.fetchAndSaveDepositProducts();
        return ResponseEntity.ok("금융 데이터 저장 완료!");
    }
}
