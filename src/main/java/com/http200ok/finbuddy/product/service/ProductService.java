package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.product.dto.PagedResponseDto;
import com.http200ok.finbuddy.product.dto.ProductDto;
import com.http200ok.finbuddy.product.dto.RecommendedProductDto;

import java.util.List;

public interface ProductService {
    List<RecommendedProductDto> getTopRecommendedProducts();
    PagedResponseDto<ProductDto> searchDepositProductsByNameAndBank(String name, String bankName, int page);
    PagedResponseDto<ProductDto> searchSavingProductsByNameAndBank(String name, String bankName, int page);
}
