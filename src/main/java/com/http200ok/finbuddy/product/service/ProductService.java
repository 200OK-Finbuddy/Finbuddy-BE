package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.product.dto.RecommendedProductDto;

import java.util.List;

public interface ProductService {
    List<RecommendedProductDto> getTopRecommendedProducts();
}
