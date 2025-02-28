package com.http200ok.finbuddy.transfer.repository;

import com.http200ok.finbuddy.transfer.domain.AutoTransfer;
import com.http200ok.finbuddy.transfer.domain.AutoTransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoTransferRepository extends JpaRepository<AutoTransfer, Long> {
    List<AutoTransfer> findByStatus(AutoTransferStatus status);

    // 특정 회원의 자동이체 목록 조회 (account.member.id 기준)
    List<AutoTransfer> findByAccount_Member_Id(Long memberId);

    // 자동이체 날짜와 활성/비활성 상태
    List<AutoTransfer> findByTransferDayAndStatus(Integer transferDay, AutoTransferStatus status);
}
