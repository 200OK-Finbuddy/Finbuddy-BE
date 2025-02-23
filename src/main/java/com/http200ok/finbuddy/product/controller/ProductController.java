package com.http200ok.finbuddy.product.controller;

import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.dto.DepositProductDto;
import com.http200ok.finbuddy.product.dto.PagedResponseDto;
import com.http200ok.finbuddy.product.dto.ProductDto;
import com.http200ok.finbuddy.product.dto.SavingProductDto;
import com.http200ok.finbuddy.product.service.DepositProductService;
import com.http200ok.finbuddy.product.service.SavingProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
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

    /**
     * 예금 상품 목록 조회 (페이징 처리) - 최신순
     */
    @GetMapping("/deposits")
    public ResponseEntity<PagedResponseDto<ProductDto>> getDepositProducts(@RequestParam(value = "page", defaultValue = "0") int page) {
        PagedResponseDto<ProductDto> depositProducts = depositProductService.getDepositProductsSortedByDisclosureStartDate(page);
        return ResponseEntity.ok(depositProducts);
    }

    /**
     * 예금 상품 ID로 상세 조회
     */
    @GetMapping("/deposit/{productId}")
    public ResponseEntity<DepositProductDto> getDepositProductById(@PathVariable("productId") Long productId) {
        DepositProductDto depositProduct = depositProductService.getDepositProductById(productId);
        return ResponseEntity.ok(depositProduct);
    }

    /**
     * 적금 상품 목록 조회 (페이징 처리)
     */
    @GetMapping("/savings")
    public ResponseEntity<PagedResponseDto<ProductDto>> getSavingProducts(@RequestParam(value = "page", defaultValue = "0") int page) {
        PagedResponseDto<ProductDto> savingProducts = savingProductService.getSavingProductsSortedByDisclosureStartDate(page);
        return ResponseEntity.ok(savingProducts);
    }

    /**
     * 적금 상품 ID로 상세 조회
     */
    @GetMapping("/saving/{productId}")
    public ResponseEntity<SavingProductDto> getSavingProductById(@PathVariable("productId") Long productId) {
        SavingProductDto savingProduct = savingProductService.getSavingProductById(productId);
        return ResponseEntity.ok(savingProduct);
    }
}
