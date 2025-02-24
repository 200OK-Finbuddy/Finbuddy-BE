package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.product.dto.DepositProductDto;
import com.http200ok.finbuddy.product.dto.PagedResponseDto;
import com.http200ok.finbuddy.product.dto.ProductDto;

public interface DepositProductService {
    void fetchAndSaveDepositProducts();
    DepositProductDto getDepositProductById(Long productId);
}
