package com.http200ok.finbuddy.product.service;

import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.domain.Product;
import com.http200ok.finbuddy.product.domain.ProductOption;
import com.http200ok.finbuddy.product.domain.SavingProduct;
import com.http200ok.finbuddy.product.dto.RecommendedProductDto;
import com.http200ok.finbuddy.product.repository.DepositProductRepository;
import com.http200ok.finbuddy.product.repository.SavingProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final DepositProductRepository depositProductRepository;
    private final SavingProductRepository savingProductRepository;

    @Override
    public List<RecommendedProductDto> getTopRecommendedProducts() {
        // 예금 3개 조회
        List<DepositProduct> depositProducts = depositProductRepository.findTop3DepositProductsByMaxInterestRate(PageRequest.of(0, 3));
        List<RecommendedProductDto> depositDtos = depositProducts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 적금 3개 조회
        List<SavingProduct> savingProducts = savingProductRepository.findTop3SavingProductsByMaxInterestRate(PageRequest.of(0, 3));
        List<RecommendedProductDto> savingDtos = savingProducts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 예금-적금-예금-적금-예금-적금 순서로 정렬
        return interleaveLists(depositDtos, savingDtos);
    }

    private RecommendedProductDto convertToDto(Product product) {
        List<? extends ProductOption> options = product instanceof DepositProduct
                ? ((DepositProduct) product).getOptions()
                : ((SavingProduct) product).getOptions();

        Double minInterestRate = options.stream()
                .mapToDouble(ProductOption::getInterestRate)
                .min().orElse(0.0);

        Double maxInterestRate = options.stream()
                .mapToDouble(ProductOption::getMaximumInterestRate)
                .max().orElse(0.0);

        Integer minSavingTerm = options.stream()
                .mapToInt(ProductOption::getSavingTerm)
                .min().orElse(0);

        Integer maxSavingTerm = options.stream()
                .mapToInt(ProductOption::getSavingTerm)
                .max().orElse(0);

        String interestRateTypeName = options.get(0).getInterestRateTypeName(); // 동일하다고 가정

        return new RecommendedProductDto(
                product.getId(),
                product instanceof DepositProduct ? "DEPOSIT" : "SAVING",
                product.getName(),
                product.getBank().getName(),
                product.getBank().getLogoUrl(),
                interestRateTypeName,
                minInterestRate,
                maxInterestRate,
                minSavingTerm,
                maxSavingTerm
        );
    }

    private List<RecommendedProductDto> interleaveLists(List<RecommendedProductDto> list1, List<RecommendedProductDto> list2) {
        List<RecommendedProductDto> result = new ArrayList<>();
        int maxSize = Math.max(list1.size(), list2.size());

        for (int i = 0; i < maxSize; i++) {
            if (i < list1.size()) {
                result.add(list1.get(i)); // 예금
            }
            if (i < list2.size()) {
                result.add(list2.get(i)); // 적금
            }
        }

        return result;
    }
}
