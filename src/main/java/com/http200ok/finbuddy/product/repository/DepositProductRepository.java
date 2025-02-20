package com.http200ok.finbuddy.product.repository;

import com.http200ok.finbuddy.bank.domain.Bank;
import com.http200ok.finbuddy.product.domain.DepositProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepositProductRepository extends JpaRepository<DepositProduct, Long> {
    Optional<DepositProduct> findByNameAndBank(String name, Bank bank);
    Page<DepositProduct> findAllByOrderByDisclosureStartDateDesc(Pageable pageable);
}
