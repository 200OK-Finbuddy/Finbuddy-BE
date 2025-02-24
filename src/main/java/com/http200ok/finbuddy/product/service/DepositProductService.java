package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.product.dto.DepositProductDto;

public interface DepositProductService {
    void fetchAndSaveDepositProducts();
    DepositProductDto getDepositProductById(Long productId);
}
