package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.product.dto.SavingProductDto;

public interface SavingProductService {
    void fetchAndSaveSavingProducts();
    SavingProductDto getSavingProductById(Long productId);
}
