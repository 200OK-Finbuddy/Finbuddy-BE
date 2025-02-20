package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.dto.DepositProductDto;
import com.http200ok.finbuddy.product.dto.PagedResponseDto;
import com.http200ok.finbuddy.product.dto.ProductDto;
import org.springframework.data.domain.Page;

public interface DepositProductService {
    void fetchAndSaveDepositProducts();
    PagedResponseDto<ProductDto> getDepositProductsSortedByDisclosureStartDate(int page);
    DepositProductDto getDepositProductById(Long productId);
}
