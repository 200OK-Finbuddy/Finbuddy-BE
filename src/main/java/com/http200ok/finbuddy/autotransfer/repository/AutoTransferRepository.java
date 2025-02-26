package com.http200ok.finbuddy.autotransfer.repository;

import com.http200ok.finbuddy.autotransfer.domain.AutoTransfer;
import com.http200ok.finbuddy.autotransfer.domain.AutoTransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoTransferRepository extends JpaRepository<AutoTransfer, Long> {
    List<AutoTransfer> findByStatus(AutoTransferStatus status);

}
