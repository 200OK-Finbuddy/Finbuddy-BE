package com.http200ok.finbuddy.product.repository;

import com.http200ok.finbuddy.bank.domain.Bank;
import com.http200ok.finbuddy.product.domain.DepositProduct;
import com.http200ok.finbuddy.product.domain.SavingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingProductRepository extends JpaRepository<SavingProduct, Long> {
    Optional<SavingProduct> findByNameAndBank(String name, Bank bank);
}
