package com.http200ok.finbuddy.product.controller;

import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.dto.*;
import com.http200ok.finbuddy.product.service.DepositProductService;
import com.http200ok.finbuddy.product.service.ProductService;
import com.http200ok.finbuddy.product.service.SavingProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final DepositProductService depositProductService;
    private final SavingProductService savingProductService;
    private final ProductService productService;

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
     * 상품명(name), 은행명(bankName) 검색
     */
    @GetMapping("/deposits")
    public ResponseEntity<PagedResponseDto<ProductDto>> getDepositProducts(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "bankName", required = false, defaultValue = "") String bankName,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        PagedResponseDto<ProductDto> depositProducts = productService.searchDepositProductsByNameAndBank(name, bankName, page);

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
     * 적금 상품 목록 조회 (페이징 처리) - 최신순
     * 상품명(name), 은행명(bankName) 검색
     */
    @GetMapping("/savings")
    public ResponseEntity<PagedResponseDto<ProductDto>> getSavingProducts(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "bankName", required = false, defaultValue = "") String bankName,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        PagedResponseDto<ProductDto> savingProducts =
                productService.searchSavingProductsByNameAndBank(name, bankName, page);

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

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendedProductDto>> getRecommendedProducts() {
        List<RecommendedProductDto> response = productService.getTopRecommendedProducts();
        return ResponseEntity.ok(response);
    }
}
