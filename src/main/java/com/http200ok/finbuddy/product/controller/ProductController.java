package com.http200ok.finbuddy.product.controller;

import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.dto.DepositProductDto;
import com.http200ok.finbuddy.product.dto.PagedResponseDto;
import com.http200ok.finbuddy.product.dto.ProductDto;
import com.http200ok.finbuddy.product.service.DepositProductService;
import com.http200ok.finbuddy.product.service.SavingProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 최신순으로 DepositProduct 목록 반환 (페이징 적용)
    @GetMapping("/deposits")
    public ResponseEntity<PagedResponseDto<ProductDto>> getDepositProducts(@RequestParam(value = "page", defaultValue = "0") int page) {
        PagedResponseDto<ProductDto> depositProducts = depositProductService.getDepositProductsSortedByDisclosureStartDate(page);
        return ResponseEntity.ok(depositProducts);
    }

    // 상품 ID로 조회하여 은행 이름, 상품 정보, 옵션 리스트 반환
    @GetMapping("/deposit/{productId}")
    public ResponseEntity<DepositProductDto> getDepositProductById(@PathVariable("productId") Long productId) {
        DepositProductDto depositProduct = depositProductService.getDepositProductById(productId);
        return ResponseEntity.ok(depositProduct);
    }
}
